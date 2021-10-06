//
//  Accounter_iPad_native_menuViewController.m
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/2/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "Accounter_iPad_native_menuViewController.h"

@implementation Accounter_iPad_native_menuViewController

- (void)dealloc
{
    
    [toolBar release];
    [mainView release];
    [webBrowser release];
    [webBrowser release];
    [webBrowser release];
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
}

#pragma mark - View lifecycle


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    
    [super viewDidLoad];
    
        
    /*
     if the applicaiton is running in debug mode or testing the make value == FALSE
     else for runnning in live or before releasing the application make value == TRUE
     */
    [self isLive:TRUE];

}

-(void)isLive:(bool)value{
    if(value == TRUE){
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
        
        [self connect];
    }
    else{
                alert = [[OCPromptView alloc] initWithPrompt:@"Enter Address" delegate:self cancelButtonTitle:@"Go Live" acceptButtonTitle:@"OK"];
                [alert setText:@"http://192.168.0.120/"];
                [alert show];
    }
}

-(void)connect{
    
    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    NSFileManager *fileMgr = [[[NSFileManager alloc] init] autorelease];
    NSError *error = nil;
    NSArray *directoryContents = [fileMgr contentsOfDirectoryAtPath:documentsDirectory error:&error];
    if (error == nil) {
        for (NSString *path in directoryContents) {
            NSString *fullPath = [documentsDirectory stringByAppendingPathComponent:path];
            BOOL removeSuccess = [fileMgr removeItemAtPath:fullPath error:&error];
            if (!removeSuccess) {
                NSLog(@"errro deleting : %@",[error localizedDescription]);
            }
        }
    } else {
         NSLog(@"errro deleting : %@",[error localizedDescription]);
    }
    
    /**Check for network connection**/
    internetReachable = [[Reachability reachabilityForInternetConnection] retain];
    [internetReachable startNotifier];
    hostReachable = [[Reachability reachabilityWithHostName: mainPathLocation] retain];
    [hostReachable startNotifier];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(checkNetworkStatus:) name:kReachabilityChangedNotification object:nil];
    
    CacheUpdater *updater = [[CacheUpdater alloc]init];
    [updater setView:mainView];
    [updater initUpdating:mainPathLocation];
    
    
    NSURL *url = [NSURL URLWithString:[mainPathLocation stringByAppendingString:@"main/login"]];
	NSMutableURLRequest *requestObj = [NSMutableURLRequest requestWithURL:url];
    [requestObj setValue:@"1.1" forHTTPHeaderField:@"Ipadapp"]; 
    [webBrowser loadRequest:requestObj];
    [webBrowser setDelegate:self];
    
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:requestObj delegate:self];
    [connection start];
    
   [CustomURLProtocol registerSpecialProtocol];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    
	if (buttonIndex != [alertView cancelButtonIndex]) {
        mainPathLocation = [[NSString alloc]initWithString:[alert enteredText]];
	}else{
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
    
    [self connect];

    
}

- (void)viewDidUnload
{
    
    [menuLinksDictonary release];
    [menuArray release];
    [accounterMenuArray release];

    [toolBar release];
    toolBar = nil;
    [mainView release];
    mainView = nil;
    [webBrowser release];
    webBrowser = nil;
    [webBrowser release];
    webBrowser = nil;
    [webBrowser release];
    webBrowser = nil;
    [super viewDidUnload];
    
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft);
}

-(void)startParsing{
    
    menuArray  = [[NSMutableArray alloc]init];
    accounterMenuArray = [[NSMutableArray alloc]init];
    isMainMenu =  false;
    menuLinksDictonary = [[NSMutableDictionary alloc]init];
    isMenuItem = false;
    NSString *filePath = [mainPathLocation stringByAppendingString:@"desktop/mac/newmenu"];
    
    NSURL * menuURL = [NSURL URLWithString:filePath];
    
    
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:menuURL];
    [request setDelegate:self];
    [request startAsynchronous];
    
  
}


-(void)requestFinished:(ASIHTTPRequest *)request{
    
    NSLog(@"%d",[request responseStatusCode]);
    
    NSXMLParser * parser = [[NSXMLParser alloc] initWithData:[request responseData]];
    [parser setDelegate:self];
    [parser parse];
    [parser release];    
    
    
}

-(BOOL)getViewTitle:(NSURL*)url{
    
    return true;
}

#pragma mark -
#pragma mark web delegates


- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    
    NSString *requestedURL = [[request URL]absoluteString];
   // NSLog(@"request : %@",requestedURL);
    if([requestedURL isEqualToString:[mainPathLocation stringByAppendingString:@"company/accounter#dashBoard" ]]){
        [self startParsing]; 
    }
    
    if([[request URL]path]!=nil){
        
        NSString *unrequiredPath = [[NSString alloc]initWithString:[[request URL]path]];
        
        if(([unrequiredPath isEqualToString:@"/site/termsandconditions"])||([unrequiredPath isEqualToString:@"/site/privacypolicy"])||([unrequiredPath isEqualToString:@"/site/support"])||([unrequiredPath isEqualToString:@"c/ontent/termsandconditions"])||([unrequiredPath isEqualToString:@"/content/privacypolicy"])||([unrequiredPath isEqualToString:@"/contact"])){
            NSString *str = [unrequiredPath substringWithRange:NSMakeRange(1, [unrequiredPath length]-1)];
            [[UIApplication sharedApplication]openURL:[NSURL URLWithString:[mainPathLocation stringByAppendingString:str]]];
            return NO;
        }
    }
    
//    BOOL missingHeaders = YES;
//    NSLog(@"Loading request with HTTP headers %@", [request allHTTPHeaderFields]);
//    NSArray *currentHeaders = [[request allHTTPHeaderFields] allKeys];
//    
//    if([currentHeaders count]<1){
//        return YES;
//    }
//    
//    for(NSString *key in currentHeaders) {
//        if([key isEqual:@"Referer"]){
//            return YES;
//        }
//        if([key  isEqualToString:@"Ipadapp"]) {
//            missingHeaders = NO;
//            break;
//        }
//    }
//    
//    if(missingHeaders) {
//        NSMutableURLRequest *newRequest = [request mutableCopy];
//        [newRequest addValue:@"1.1" forHTTPHeaderField:@"Ipadapp"];
//        [webBrowser loadRequest:newRequest];
//        return NO;
//    }
	return YES;
    
}


-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    NSLog(@"error : %@,%@",[[webView request]URL],[error localizedDescription]);
    
//    LGViewHUD *HUD = [LGViewHUD defaultHUD];
//    HUD.image=[UIImage imageNamed:@"rounded-fail.png"];
//    HUD.topText=@"Error";
//    HUD.bottomText=[error localizedDescription];  
//    [HUD showInView:mainView];
    


}

- (void)webViewDidStartLoad:(UIWebView *)webView {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
}


#pragma mark -
#pragma mark xml file delegates



- (void)parserDidStartDocument:(NSXMLParser *)parser {
    NSLog(@"parser starting:");
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qualifiedName attributes:(NSDictionary *)attributeDict {
    
    
    
    if([elementName isEqualToString:@"Menu"])
    {
        NSString *name = [attributeDict objectForKey:@"text"];
        isMainMenu = !isMainMenu;
        
        AMenu *amenu = [[AMenu alloc]init];
        [amenu setMenuName:name];
        [amenu setMenuItem];
        
        if(isMainMenu){
            [menuArray addObject:amenu];
        }else{
            [[[menuArray lastObject] getMenuItems] addObject:amenu];
        }
        [amenu release];
        
    }
    else if([elementName isEqualToString:@"MenuItem"])
    {
        isMenuItem = true;
        NSString *name = [attributeDict objectForKey:@"text"];
        AMenuItem *item = [[AMenuItem alloc]init];
        [item setMenuName:name];
        
        if(isMainMenu){
            [[[menuArray lastObject]getMenuItems] addObject:item];
        }else{
            NSMutableArray *items = [[NSMutableArray alloc]initWithArray:[[menuArray lastObject]getMenuItems]];
            if([items count]>0){
                if([[[items lastObject]class] isSubclassOfClass:[AMenu class]]){
                    [[[items lastObject] getMenuItems]addObject:item];
                }
            }else{
                [accounterMenuArray addObject:item];
            }
            [items  release];
        }
        [item  release];
    }
    else if([elementName isEqualToString:@"Seperator"])
    {
        
    }
    
    
}

- (void)parser:(NSXMLParser *)parser
 didEndElement:(NSString *)elementName
  namespaceURI:(NSString *)namespaceURI
 qualifiedName:(NSString *)qName
{
    
    if([elementName isEqualToString:@"MenuItem"])
    {
        isMenuItem = false;
    }
    else if([elementName isEqualToString:@"Menu"])
    {
        isMainMenu = !isMainMenu;
    }
    else if([elementName isEqualToString:@"Seperator"])
    {
        
    }
    
}
-(void)setViewTitle{
    NSLog(@"here");
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    
    if(!isMenuItem){
        return;
    }
    
    if([menuArray count]>0){
        NSMutableArray *items = [[NSMutableArray alloc]initWithArray:[[menuArray lastObject] getMenuItems]];
        if([items count]>0){
            if([[[items lastObject]class]isSubclassOfClass:[AMenuItem class]]){
                [(AMenuItem*)[items lastObject]setMenuURL:string];
            }else{
                NSMutableArray *item2 = [[NSMutableArray alloc]initWithArray:[(AMenu*)[items lastObject] getMenuItems]];
                if([item2 count]>0){
                    [(AMenuItem*)[item2 lastObject]setMenuURL:string];
                }
                [item2 release];
            }
        }
        [items release];
    }else{
        [[accounterMenuArray lastObject]setMenuURL:string];
    }
    
}

- (void)parserDidEndDocument:(NSXMLParser *)parser {
    
    [MenuCreator initWithMenuArray:menuArray :accounterMenuArray:toolBar:mainView :self];
    
}
-(void)changeBrowserUrl:(NSString*)newUrl{
    
    
    NSString *urlLocation = [mainPathLocation stringByAppendingString:newUrl];
    NSURL *url = [NSURL URLWithString:urlLocation];
	NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    [webBrowser loadRequest:requestObj];
    [webBrowser stringByEvaluatingJavaScriptFromString:@"generateTitle"];
    
}


#pragma mark-
#pragma mark check network

- (void) checkNetworkStatus:(NSNotification *)notice
{
    
    
    NetworkStatus internetStatus = [internetReachable currentReachabilityStatus];
    switch (internetStatus)
    
    {
        case NotReachable:
        {
            NSLog(@"unable to connect");
//           UIAlertView *networkAlert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Unable to connect to Accounter server please check your connection" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
//
//            [networkAlert show];
//            [networkAlert release];
            break;
        }
        case ReachableViaWiFi:
        {
            NSLog(@"connected");

            break;
        }
        case ReachableViaWWAN:
        {
                NSLog(@"connected");
            break;
        }
    }
    
    NetworkStatus hostStatus = [hostReachable currentReachabilityStatus];
    switch (hostStatus)
    
    {
        case NotReachable:
        {
                        NSLog(@"unable to connect");
//            UIAlertView *networkAlert = [[UIAlertView alloc]initWithTitle:@"Error" message:@"Unable to connect to Accounter server please check your connection" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
//            
//            [networkAlert show];
//            [networkAlert release];
            break;
        }
        case ReachableViaWiFi:
        {
                NSLog(@"connected");
            break;
        }
        case ReachableViaWWAN:
        {
                NSLog(@"connected");
            break;
        }
    }
}



//- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace {
//    return [protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust];
//}
//
//- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
////    if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
////        if ([trustedHosts containsObject:challenge.protectionSpace.host])
////            [challenge.sender useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust] forAuthenticationChallenge:challenge];
////    
////    [challenge.sender continueWithoutCredentialForAuthenticationChallenge:challenge];
//    
//    if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
//    {
////        [challenge.sender useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust] forAuthenticationChallenge:challenge];
//    [challenge.sender continueWithoutCredentialForAuthenticationChallenge:challenge];
//    }
//    else
//    {
//        [challenge.sender continueWithoutCredentialForAuthenticationChallenge:challenge];
//    }
//
//}


@end
