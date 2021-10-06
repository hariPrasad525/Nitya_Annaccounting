//
//  MyClass.m
//  Accounter
//
//  Created by Amrit Mishra on 6/25/11.
//  Copyright 2011 Vimukti Technologies Pvt. Ltd. All rights reserved.
//

#import "MainClass.h"


@implementation MyClass



- (id)init
{
    self = [super init];
    if (self) {
        
        inc = 0;
        urlAdded = FALSE;
        createWebScript = FALSE;
        _loading = NO;
        applicationOpen = FALSE;
        localServer = FALSE;
        webPageLoadingComplete = FALSE;
        mainPathLocation  = [[NSString alloc]init];
        
        itemName = [[NSMutableArray alloc]init];
        itemLinks  = [[NSMutableArray alloc]init];
        
        currentNetStat = UnKnown;
        currentHostStat = UnKnown;
        
        
        NSString *questions = [[NSBundle mainBundle] pathForResource:@"XMLPath" ofType:@"plist"];
        
        if ([[NSFileManager defaultManager] fileExistsAtPath:questions]) {
            NSMutableArray *array = [[NSMutableArray alloc] initWithContentsOfFile:questions];
            for (NSString *str1 in array)
            {
                mainPathLocation = [str1 retain];
                break;
            }
            [array release];
        }
    }
    return self;
}



- (void)dealloc
{
    [itemName release];
    [itemLinks release];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [str release];
    [super dealloc];
}


- (void)windowWillClose:(NSNotification *)aNotification {
    [NSApp terminate:self];
}


//this checks teh receipt of the application before starting the app.
//
//-(void)applicationDidFinishLaunching:(NSNotification *)notification{
//    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
//    [prefs setObject:@"1212121" forKey:@"cacheVersion"];
//    [prefs synchronize];
//}

- (void)appDidLaunch:(NSNotification*)note
{
    if(!applicationOpen){
        applicationOpen = TRUE;
        
        /*  enable the following lines if you want to enable debugging dialogue. This will enable application to open any address*/
             //  [localaddressTextField setEnabled:FALSE];
             //   [NSApp beginSheet:openingURLpanel modalForWindow:mainWindow modalDelegate:self didEndSelector: NULL contextInfo:nil];
               
        
        /*add these lines and comment the above two lines while releasing in live to remove the debugging dialogue and anble app to get live server address*/       
       [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(checkNetworkStatus:) name:kReachabilityChangedNotification object:nil];
        [self InitiateURL:mainPathLocation];
        NSString *xmlFinalPath = [[NSString alloc]init];
        xmlFinalPath = [[NSString alloc]initWithString:[mainPathLocation stringByAppendingString:@"desktop/mac/newmenu"]];         
        menuCreator = [[AccounterMenuCreator alloc]initWith:xmlFinalPath :mainMenu :accounterMenu :self];
        
    }
}



-(void)awakeFromNib
{
    
	[mainWindow setDelegate:self];
    [mainWindow setFrame:[mainWindow frameRectForContentRect:[[mainWindow screen] frame]] display:YES animate:YES];
    
    [[[NSWorkspace sharedWorkspace] notificationCenter] addObserver:self selector:@selector(appDidLaunch:) name:NSWorkspaceDidLaunchApplicationNotification object:nil];
}



#pragma mark -
#pragma mark load files & web page

/**
 append main/login to the url and initiate webview to load accounter
 **/
-(void)LoadOpeningURL:(NSString *)url
{

    NSString *openingUrl = [url stringByAppendingString:@"main/login"];
    
    /*
     Add cacheupdater class to enable saving the files in the disk.
     */
    [cacheProgressBar startAnimation:self];
    CacheUpdater *updater = [[CacheUpdater alloc]init];
    [updater setMainWindow:mainWindow :cacheUpdaterView];
    [updater initUpdating:url];
 
    
    /*
     add custom url protocol cache
     */
    [CustomURLProtocol registerSpecialProtocol];
    
    /*
     this will make sure the previous file saved by webview(not cache) are deleted. this is needed to make sure our changes in css appear
     */
    [[WebHistory optionalSharedHistory]removeAllItems];
    
    /*create request for the main url, add the new HTTPheader to that request
     */
    mainRequest = [[[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:openingUrl]]autorelease];
    [mainRequest setValue:@"Accounter.live.in" forHTTPHeaderField:@"NativeApp"];
    [[webPage mainFrame] loadRequest:mainRequest];
    [webPage setPolicyDelegate:self];
	[webPage setUIDelegate:self];
    NSLog(@"Launching server connectin to : %@",[mainRequest URL]);
        
    /*
     for showing progress bar for the application.
     */
    [longProgressBar bind:@"value" toObject:webPage withKeyPath:@"estimatedProgress" options:nil];
    [longProgressBar startAnimation:nil];
}


-(void)reloadMenu
{
    [menuCreator create];
}
-(void)clearMenu{
    [menuCreator clear];
}

#pragma mark -
#pragma mark javascript methods being called.



/* This method is called by the WebView when it is deciding what
 methods on this object can be called by JavaScript.  The method
 should return NO the methods we would like to be able to call from
 JavaScript, and YES for all of the methods that cannot be called
 from JavaScript.
 */
+ (BOOL)isSelectorExcludedFromWebScript:(SEL)selector {
	//NSLog(@"%@ received %@ for '%@'", self, NSStringFromSelector(_cmd), NSStringFromSelector(selector));
    if([NSStringFromSelector(selector) isEqualToString:@"reloadMenu"])
        return NO;
    if([NSStringFromSelector(selector) isEqualToString:@"clearMenu"])
        return NO;
    else
        return YES;
}


/* This method is called by the WebView to decide what instance
 variables should be shared with JavaScript.  The method should
 return NO for all of the instance variables that should be shared
 between JavaScript and Objective-C, and YES for all others.
 */
+ (BOOL)isKeyExcludedFromWebScript:(const char *)property {
    NSString *foo = @"reloadMenu";
    NSString *foo1 = @"clearMenu";
    
    const char *bar = [foo UTF8String]; 
    const char *bar1 = [foo1 UTF8String];
    if(property == bar)
        return NO;
    if(property == bar1)
        return NO;
    else
        return YES;
}
/* This method converts a selector value into the name we'll be using
 to refer to it in JavaScript.  here, we are providing the following
 Objective-C to JavaScript name mappings:
 'doOutputToLog:' => 'log'
 'changeJavaScriptText:' => 'setscript'
 With these mappings in place, a JavaScript call to 'console.log' will
 call through to the doOutputToLog: Objective-C method, and a JavaScript call
 to console.setscript will call through to the changeJavaScriptText:
 Objective-C method.  
 
 Comments for the webScriptNameForSelector: method in WebScriptObject.h talk more
 about the default name conversions performed from Objective-C to JavaScript names.
 You can overrride those defaults by providing your own translations in your
 webScriptNameForSelector: method.
 */
+ (NSString *) webScriptNameForSelector:(SEL)sel {
	//NSLog(@"%@ received %@ with sel='%@'", self, NSStringFromSelector(_cmd), NSStringFromSelector(sel));
    return nil;
}




#pragma mark -
#pragma mark download delegates


/* NSURLDownload Delegate Methods */

- (void)downloadDidBegin:(NSURLDownload *)download
{
	// This is where you would do something nice in terms of UI.
    
}

- (void)download:(NSURLDownload *)download didReceiveDataOfLength:(unsigned)length
{
	// Data received.  Update a progress bar somewhere
    
    
}

- (void)download:(NSURLDownload *)download didReceiveResponse:(NSURLResponse *)response
{
	// A response was received.  We can use the response to get some interesting stuff like the expected content length
	// for use in progress indicators and a suggested filename that I'll be using here..
	downloadPath = [[response suggestedFilename] retain];
}

- (void)download:(NSURLDownload *)download decideDestinationWithSuggestedFilename:(NSString *)filename
{
	NSSavePanel *panel = [NSSavePanel savePanel];
	if ([panel runModalForDirectory:nil file:downloadPath] == NSFileHandlingPanelCancelButton)
	{
		// If the user doesn't want to save cancel the download.
		[download cancel];
	}
	else
	{
		// Set the destination to save to.
		[download setDestination:[panel filename] allowOverwrite:YES];
	}
}
- (void)webView:(WebView *)sender decidePolicyForMIMEType:(NSString *)type request:(NSURLRequest *)request frame:(WebFrame *)frame decisionListener:(id<WebPolicyDecisionListener>)listener
{
    
	if ([[sender class] canShowMIMEType:type]) // Why sender class.  In case this is actually a WebView subclass that can show extra mime types
	{
        
        if([type isEqualToString:@"text/html"]){
            [listener use];
        }else{
            [listener download]; 
        }
	}
	else
	{
		// Download that!
		[listener download];
	}
    
}


- (void)downloadDidFinish:(NSURLDownload *)download {
    
    
    NSAlert *alert = [NSAlert alertWithMessageText:@"Download completed" defaultButton:@"Ok" alternateButton:nil otherButton:nil informativeTextWithFormat:@""];
    [alert runModal];
}

- (void)download:(NSURLDownload *)download didFailWithError:(NSError *)error {
    [self presentDownloadError];
}

- (void)presentDownloadSuccess {
    
}

- (void)presentDownloadError {
    
}


#pragma mark -
#pragma mark opening url actions

- (IBAction)lanchUrlSelected:(id)sender {
       
    if(localServer==FALSE){
        [self InitiateURL:mainPathLocation];

    }else if (localServer==TRUE) {
        [self InitiateURL:[localaddressTextField stringValue]];
    }
    
    NSString *xmlFinalPath = [[NSString alloc]init];
    if(localServer == FALSE){
        xmlFinalPath = [[NSString alloc]initWithString:[mainPathLocation stringByAppendingString:@"desktop/mac/newmenu"]];         
    }else{
        xmlFinalPath = [[NSString alloc]initWithString:[[localaddressTextField stringValue] stringByAppendingString:@"desktop/mac/newmenu"]];    
    }
    [openingURLpanel orderOut:nil];
    [NSApp endSheet:openingURLpanel];
    
    menuCreator = [[AccounterMenuCreator alloc]initWith:xmlFinalPath :mainMenu :accounterMenu :self];
    
}

- (IBAction)changeLaunchUrl:(id)sender {
    NSButtonCell *sellCel = [checkURLs selectedCell];
    int tag = [sellCel tag];
    
    if(tag==1){
        [localaddressTextField setEnabled:FALSE];
        localServer = FALSE;
    }else if(tag==2){
        [localaddressTextField setEnabled:FALSE];
         localServer = FALSE;
        mainPathLocation = @"https://next.accounterlive.com/";
    }else if(tag ==3){
        [localaddressTextField setEnabled:TRUE];
        localServer = TRUE;

    }
}

-(void)InitiateURL:(NSString *)url{
    
    
    [helpMenu insertItemWithTitle:@"Contact Developer" action:@selector(sendFeedback:) keyEquivalent:@"" atIndex:0];
    
    [self LoadOpeningURL:url];
    
    //required to remove http:// from mainPathLocation//
    NSRange range = [mainPathLocation rangeOfString:@"//"];
    NSString *substring = [[mainPathLocation substringFromIndex:NSMaxRange(range)] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    substring = [substring substringToIndex:[substring length]-1];
    
    /**Check for network connection**/
    internetReachable = [[Reachability reachabilityForInternetConnection] retain];
    [internetReachable startNotifier];
    hostReachable = [[Reachability reachabilityWithHostName: substring] retain];
    [hostReachable startNotifier];
    
    
    
}

#pragma mark -
#pragma mark external link handling

/* These methods handles if any external link inside teh webview is clicked. it will open in the default browser*/

- (void)webView:(WebView *)sender
mouseDidMoveOverElement:(NSDictionary *)elementInformation
  modifierFlags:(unsigned int)modifierFlags
{
	if( ! _loading ) {
    	
    	NSURL *link = [elementInformation objectForKey: WebElementLinkURLKey];
    	if( link ) {
        	//WebFrame *target = [elementInformation objectForKey: WebElementLinkTargetFrameKey];
            
    	}
	}
}
- (BOOL) _sendBrowserRequest: (NSURLRequest*)request forAction: (NSDictionary*)actionInformation
{
	NSWorkspaceLaunchOptions options = NSWorkspaceLaunchDefault;
	unsigned modifiers = [[actionInformation objectForKey: WebActionModifierFlagsKey] unsignedIntValue];
	if( (modifiers & NSShiftKeyMask) )
    	options |= NSWorkspaceLaunchWithoutActivation;
	BOOL ok = [[NSWorkspace sharedWorkspace] openURLs: [NSArray arrayWithObject: [request URL]]
                              withAppBundleIdentifier: nil
                                              options: options
                       additionalEventParamDescriptor: nil
                                	launchIdentifiers: NULL];
	if( ! ok )
    	NSBeep();
	return ok;
}


/**check if the url contains go premium or not and stops the application from letting it open in new window**/
- (void)webView:(WebView *)sender decidePolicyForNewWindowAction:(NSDictionary *)actionInformation
    	request:(NSURLRequest *)request
   newFrameName:(NSString *)frameName
decisionListener:(id)listener
{
    NSString *host = [[request URL]absoluteString];
    NSLog(@"host:%@",host);

        [self _sendBrowserRequest: request forAction: actionInformation];
        [listener ignore];

}


- (void)webView:(WebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WebFrame *)frame {
    NSAlert *alert = [[NSAlert alloc] init];
    [alert addButtonWithTitle:@"OK"];
    [alert setMessageText:message];
    [alert runModal];
    [alert release];
}

-(void)webView:(WebView*)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message{
    
    NSAlert *alert = [[NSAlert alloc] init];
    [alert addButtonWithTitle:@"OK"];
    [alert setMessageText:message];
    [alert runModal];
    [alert release];
}


#pragma mark -
#pragma mark web delegates

/* Called when the loadin of url is completed in the webview frame  */

-(void)webView:(WebView *)sender didFinishLoadForFrame:(WebFrame *)frame
{
    _loading = NO;
    webPageLoadingComplete = TRUE;
    [longProgressBar setHidden:TRUE];
    [longProgressBar stopAnimation:nil];
}

/* Called just before a webView attempts to load a resource.  Here, we look at the
 request and if it's destined for our special protocol handler we modify the request
 so that it contains an NSDictionary containing some information we want to share
 between the code in this file and the custom NSURLProtocol.  */

- (void)webView:(WebView *)sender didStartProvisionalLoadForFrame:(WebFrame *)frame
{
    _loading = YES;

}

- (void)webView:(WebView *)sender runOpenPanelForFileButtonWithResultListener:(id < WebOpenPanelResultListener >)resultListener
{       
    NSOpenPanel* openDlg = [NSOpenPanel openPanel];
    [openDlg setCanChooseFiles:YES];
    [openDlg setCanChooseDirectories:NO];
    
    if ( [openDlg runModalForDirectory:NSHomeDirectory() file:nil] == NSOKButton )
    {
        NSArray* files = [openDlg filenames];
        {
            NSString* fileName = [files objectAtIndex:0];
            [resultListener chooseFilename:fileName]; 
        }
    }
}
- (void)webView:(WebView *)sender didClearWindowObject:(WebScriptObject *)windowObject forFrame:(WebFrame *)frame
{
    [windowObject setValue:self forKey:@"macclient"];
}



-(NSURLRequest *)webView:(WebView *)sender
                resource:(id)identifier
         willSendRequest:(NSURLRequest *)request
        redirectResponse:(NSURLResponse *)redirectResponse		
          fromDataSource:(WebDataSource *)dataSource
{
    
    NSDictionary *headers = [request allHTTPHeaderFields];
    NSMutableURLRequest *newRequest = [[request mutableCopy]autorelease];
    
    if([headers valueForKey:@"Nativeapp"]== nil)
    {
        [newRequest setValue:@"Accounter.live.in" forHTTPHeaderField:@"Nativeapp"]; 
    }
    return newRequest;
    
}



-(void)changeWebPageLink:(NSString*)link{
    NSString *urlMaking ;
    
    
    if(localServer == FALSE){
        urlMaking = [mainPathLocation stringByAppendingString:link];
    }else{
        urlMaking = [[localaddressTextField stringValue] stringByAppendingString:link];
    }
    
    if([[webPage mainFrameURL]isEqualToString:urlMaking]==FALSE){
        if([link length]>1)
            [[webPage mainFrame] loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:urlMaking]]];
    }
    
}

-(void)webView:(WebView *)sender plugInFailedWithError:(NSError *)error dataSource:(WebDataSource *)dataSource{
    NSLog(@"error : %@",[error localizedDescription]);
}


-(void)webView:(WebView *)sender didFailLoadWithError:(NSError *)error forFrame:(WebFrame *)frame{
    NSLog(@"error : %@",[error localizedDescription]);
}

/*
 following two methods are used to enable copy-paste functionality in the application 
 */
- (IBAction)copyWebviewText:(id)sender {
    [webPage copy:sender];
}

- (IBAction)pasteWebviewText:(id)sender {
    [webPage paste:sender];
}




#pragma mark-
#pragma mark check network

- (void) checkNetworkStatus:(NSNotification *)notice
{
    
    
    NetworkStatus internetStatus = [internetReachable currentReachabilityStatus];
    if(currentNetStat != internetStatus){
        switch (internetStatus)
        {
            case NotReachable:
            {
                if(webPageLoadingComplete == TRUE){
                    NSBeginAlertSheet(  @"Internet Error",  @"Close", nil,
                                      nil, mainWindow, self,
                                      @selector(sheetDidEndShouldClose:returnCode:contextInfo:),NULL,NULL,
                                      @"Accounter is unable to access the Internet.Check your internet connection or try relaunching the application.");
                }else{
                    NSString* filePath = [[NSBundle mainBundle] pathForResource:@"NoConnection"
                                                                         ofType:@"html"
                                                                    inDirectory:@""];
                    NSURL* fileURL = [NSURL fileURLWithPath:filePath];
                    NSURLRequest* request = [NSURLRequest requestWithURL:fileURL];
                    [[webPage mainFrame] loadRequest:request];
                }
                break;
            }
            case ReachableViaWiFi:
            {
                if(currentNetStat!=UnKnown){
                    //If it is unknown then it is first time.
                    [self InitiateURL:mainPathLocation];
                }
                break;
            }
            case ReachableViaWWAN:
            case UnKnown:
            {
                //IT WILL NOT COME FOR NOW.
                break;
            }
        }
        
    }
    currentNetStat = internetStatus;
    
    if(internetStatus == NotReachable){
        //Obviosly host will not be rechable. So no need to check for host rechable
        return;
    }
    
    NetworkStatus hostStatus = [hostReachable currentReachabilityStatus];
    if(currentHostStat == hostStatus){
        //Host Rechability not changedm then no need to check it again.
        return;
    }
    switch (hostStatus)
    
    {
        case NotReachable:
        {
            if(webPageLoadingComplete == TRUE){
                NSBeginAlertSheet(  @"Gateway Error",  @"Close", nil,
                                  nil, mainWindow, self,
                                  @selector(sheetDidEndShouldClose:returnCode:contextInfo:),NULL,NULL,
                                  @"Accounter is unable to access. Gateway to the host server is down. Please check your internet connection or contact service.");
            }else{
                NSString* filePath = [[NSBundle mainBundle] pathForResource:@"NoConnection"
                                                                     ofType:@"html"
                                                                inDirectory:@""];
                NSURL* fileURL = [NSURL fileURLWithPath:filePath];
                NSURLRequest* request = [NSURLRequest requestWithURL:fileURL];
                [[webPage mainFrame] loadRequest:request];
            }
            break;
        }
        case ReachableViaWiFi:
        {
            if(currentHostStat == NotReachable){
                //If Previously host not rechable then need to reload
                [self InitiateURL:mainPathLocation];
            }
            break;
        }
        case ReachableViaWWAN:
        {
            
            break;
        }
    }
    currentHostStat = hostStatus;
}


#pragma mark-
#pragma mark other functions

/*this method starts the mail.app in the mac OSx and initiate new mail view with the mail id set.*/
-(IBAction)sendFeedback:(id)sender
{
    NSString *encodedSubject = [NSString stringWithFormat:@"SUBJECT=%@", [@"" stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSString *encodedBody = [NSString stringWithFormat:@"BODY=%@", [@"" stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSString *encodedTo = [@"support@accounterlive.com" stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSString *encodedURLString = [NSString stringWithFormat:@"mailto:%@?%@&%@", encodedTo, encodedSubject, encodedBody];
    NSURL *mailtoURL = [NSURL URLWithString:encodedURLString];
    [[NSWorkspace sharedWorkspace] openURL:mailtoURL];
}


/* We are using this one to clear the cache if caching is implemented*/
-(IBAction)clearCache:(id)sender{
    
    
    NSFileManager* fm = [[[NSFileManager alloc] init] autorelease];
    NSString *folderPath = [NSHomeDirectory() stringByAppendingFormat:@"/Library/Application Support/Accounter/"];
    NSDirectoryEnumerator* en = [fm enumeratorAtPath:folderPath];    
    NSError* err = nil;
    BOOL res;
    
    NSString* file;
    while (file == [en nextObject]) {
        res = [fm removeItemAtPath:[folderPath stringByAppendingPathComponent:file] error:&err];
        if (!res && err) {
            
        }
    }
}


#pragma mark-
#pragma mark help panel

- (IBAction)openHelp:(id)sender {
    [NSApp beginSheet:helpPanel modalForWindow:mainWindow modalDelegate:self didEndSelector: NULL contextInfo:nil];
}

- (IBAction)closeHelpPanel:(id)sender {
    [helpPanel orderOut:nil];
    [NSApp endSheet:helpPanel];
}

- (IBAction)gpOnline:(id)sender {
    [[NSWorkspace sharedWorkspace] openURL:[NSURL URLWithString:@"http://help.accounterlive.com/"]];
}

- (void)sheetDidEndShouldClose: (NSWindow *)sheet returnCode: (NSInteger)returnCode contextInfo: (void *)contextInfo
{
    // [NSApp terminate:self]; 
    //TODO enable this if you want to close the app after the  button is closed
}



@end
