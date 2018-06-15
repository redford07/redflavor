
#include "liveMedia.hh"
#include "BasicUsageEnvironment.hh"

UsageEnvironment* env;

Boolean reuseFirstSource = False;

Boolean iFramesOnly = False;

static void announceStream(RTSPServer* rtspServer, ServerMediaSession* sms,
			   char const* streamName, char const* inputFileName); 

int main(int argc, char** argv) {

  
  TaskScheduler* scheduler = BasicTaskScheduler::createNew();
  env = BasicUsageEnvironment::createNew(*scheduler);

  UserAuthenticationDatabase* authDB = NULL;
#ifdef ACCESS_CONTROL
  authDB = new UserAuthenticationDatabase;
  authDB->addUserRecord("username1", "password1"); 
#endif

  RTSPServer* rtspServer = RTSPServer::createNew(*env, 8554, authDB);
  if (rtspServer == NULL) {
    *env << "Failed to create RTSP server: " << env->getResultMsg() << "\n";
    exit(1);
  }

  char const* descriptionString = "Session streamed by \"testRTSPServer\"";

  
  {
    char const* streamName = "h264";
    
    char const* inputFileName = "stdin";

    ServerMediaSession* sms = ServerMediaSession::createNew(*env, streamName, streamName, descriptionString);

    sms->addSubsession( H264VideoFileServerMediaSubsession ::createNew(*env, inputFileName, reuseFirstSource));

   	rtspServer->addServerMediaSession(sms);

    announceStream(rtspServer, sms, streamName, inputFileName);	
  }

  if (rtspServer->setUpTunnelingOverHTTP(80) || rtspServer->setUpTunnelingOverHTTP(8000) || rtspServer->setUpTunnelingOverHTTP(8080)) {
    *env << "\n(We use port " << rtspServer->httpServerPortNum() << " for optional RTSP-over-HTTP tunneling.)\n";
  } else {
    *env << "\n(RTSP-over-HTTP tunneling is not available.)\n";
  }

  env->taskScheduler().doEventLoop(); 

  return 0; 
}

static void announceStream(RTSPServer* rtspServer, ServerMediaSession* sms,
			   char const* streamName, char const* inputFileName) {
  char* url = rtspServer->rtspURL(sms);
  UsageEnvironment& env = rtspServer->envir();
  env << "\n\"" << streamName << "\" stream, from the file \""
      << inputFileName << "\"\n";
  env << "Play this stream using the URL \"" << url << "\"\n";
  delete[] url;
}
