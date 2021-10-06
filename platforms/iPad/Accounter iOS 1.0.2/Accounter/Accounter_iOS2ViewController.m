//
//  Accounter_iOS2ViewController.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "Accounter_iOS2ViewController.h"

#define kOFFSET_FOR_KEYBOARD 230.0
#define TOOLBAR_HEIGHT 64.0f // DEFAULT APP POSITION + ACTUAL TOOLBAR HEIGHT

@implementation Accounter_iOS2ViewController

#pragma mark -
#pragma mark  View lifecycle

- (void)dealloc
{
    [navigationBar release];
    [commandTextField release];
    [mainView release];
    [toolbar release];
    [activityIndicator release];
    [activityLabel release];
    [scrollview release];
    [toolBarCancelButton release];
    [toolBarTitle release];
    [helpButton release];
    [datePicker release];
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



- (void)viewDidLoad
{
    [self showDatePicker];
    navigationBar.translucent = NO;
    [activityIndicator startAnimating];
    [activityIndicator setHidden:FALSE];
    [activityLabel setText:@"Connecting to Server..."];
    [activityLabel setHidden:FALSE];
    
    [self ConnectToMainServer];
    
    //        alert = [[OCPromptView alloc] initWithPrompt:@"Enter Address" delegate:self cancelButtonTitle:@"Close" acceptButtonTitle:@"OK"];
    //        [alert show];
    //        [alert release];
    
}


- (void)viewDidUnload
{
    [navigationBar release];
    navigationBar = nil;
    [commandTextField release];
    commandTextField = nil;
    [mainView release];
    mainView = nil;
    [toolbar release];
    toolbar = nil;
    [activityIndicator release];
    activityIndicator = nil;
    [activityLabel release];
    activityLabel = nil;
    [scrollview release];
    scrollview = nil;
    [toolBarCancelButton release];
    toolBarCancelButton = nil;
    [toolBarTitle release];
    toolBarTitle = nil;
    [helpButton release];
    helpButton = nil;
    [datePicker release];
    datePicker = nil;
    [super viewDidUnload];
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


-(void)initiateVariables{
    
    datePickerShown = FALSE;
    sendInitializeStream = TRUE;
    showingReconnectDialogue = FALSE;
    startTimer = FALSE;
    yPosition = 0;
    // interval = 0;
    attempts = 0;
    
    commandCode = [[NSMutableDictionary alloc]init];
    //    cell1NameArray = [[NSMutableArray alloc]init];
    //    cell1ValueArray = [[NSMutableArray alloc]init];
    //    
    //    cell3NameArray = [[NSMutableArray alloc]init];
    //    cell3ValueArray = [[NSMutableArray alloc]init];
    //    cell4NameArray = [[NSMutableArray alloc]init];
    //    cell4ValueArray = [[NSMutableArray alloc]init];
    //    
    cellTableArray = [[NSMutableArray alloc]init];
    
    listCode = [[NSMutableDictionary alloc]init];
    commandTableArray = [[NSMutableArray alloc]init];
    //    cell2NameArray = [[NSMutableArray alloc]init];
    //    cell2ValueArray = [[NSMutableArray alloc]init];
    textFieldValue = [[NSString alloc]init];
    screenRect = [scrollview bounds];
    screenWidth = screenRect.size.width;
    screenHeight = screenRect.size.height;
    jsonDeserializer = [[ResultJSONDeserializer alloc]init];
    parser = [[SBJsonParser alloc] init];
}



-(void)ConnectToMainServer{
    
    // mainServerAddress = [[NSString alloc]initWithString:[alert enteredText]];
    
    mainServerAddress = @"www.accounterlive.com";
    
    scrollview = [[UIScrollView alloc]   initWithFrame:CGRectMake(0, TOOLBAR_HEIGHT, mainView.frame.size.width, mainView.frame.size.height-TOOLBAR_HEIGHT)];
    [scrollview setBackgroundColor:[UIColor clearColor]];
    scrollview.showsHorizontalScrollIndicator = FALSE;
    scrollview.showsVerticalScrollIndicator = TRUE;
    [mainView addSubview:scrollview];
    
    [self initiateVariables];
    
    [self addButtonToToolBar];
    
    [activityIndicator setFrame:CGRectMake(screenWidth/2-80, screenHeight/2-60, 20, 20)];
    [activityLabel setFrame:CGRectMake(screenWidth/2-60, screenHeight/2-60, 151, 20)];
    [scrollview addSubview:activityIndicator];
    [scrollview addSubview:activityLabel];
    
    
    [self initServerCommunication];
    [self sendCookieToServer];
    [scrollview release];
    
}


#pragma mark -
#pragma mark Common Methods


-(void)serverDisconnected{
    
    
    [activityIndicator stopAnimating];
    [activityIndicator setHidden:TRUE];
    [activityLabel setHidden:TRUE];
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    
    if(startTimer == FALSE){
        startTimer = TRUE;
        reconnectionTimer = [NSTimer scheduledTimerWithTimeInterval:1.0
                                                             target:self 
                                                           selector:@selector(timerFired:) 
                                                           userInfo:nil 
                                                            repeats:YES];
    }
    
    
    
}
-(void)serverConnected{
    
    [reconnectionTimer invalidate];
    reconnectionTimer = nil;
    showingReconnectDialogue = FALSE;
    attempts = 0;
    [activityIndicator stopAnimating];
    [activityIndicator setHidden:TRUE];
    [activityLabel setHidden:TRUE];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    
    
}

-(void)timerFired:(NSTimer *) theTimer
{    
    // NSLog(@"attempts : %d",attempts);
    
    if(attempts>20){
        [reconnectionTimer invalidate];
        reconnectionTimer = nil;
        startTimer = FALSE;
        if(showingReconnectDialogue == FALSE)
        {
            
            
            showingReconnectDialogue = TRUE;
            reconnectAlert = [[UIAlertView alloc]initWithTitle:@"Connection Error" message:@"Accounter is not able to connect to Server. Do you want to Retry?" delegate:self cancelButtonTitle:@"Retry" otherButtonTitles:@"Exit", nil];
            [reconnectAlert show];
            [reconnectAlert release];
            
        }
    }else{
        attempts++;
    }
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
	if(alertView == alert){
        
        if (buttonIndex != [alertView cancelButtonIndex]) {
            [self ConnectToMainServer];
        }else{
            exit(0);
        }
    }else{
        if (buttonIndex != [alertView cancelButtonIndex]) {
            exit(0);
        }else{
            
            showingReconnectDialogue = FALSE;
            attempts = 0;
            reconnectionTimer = [NSTimer scheduledTimerWithTimeInterval:1.0
                                                                 target:self 
                                                               selector:@selector(timerFired:) 
                                                               userInfo:nil 
                                                                repeats:YES];
        }
    }
}

-(void)resetControls{
    
    NSArray* subViews = scrollview.subviews;
    
    for( UIView *aView in subViews ) {
        if([aView isEqual:toolbar]){
            
        }else{
            [aView setHidden:YES];
            [aView removeFromSuperview];
        }
    }
    yPosition = 0;
    scrollview.contentSize = CGSizeMake(scrollview.frame.size.width,scrollview.frame.size.height);
}



- (IBAction)cancelPressed:(id)sender {
    [self sendDataToServer:@"cancel"];
}

-(IBAction)backButtonClicked:(id)sender {
    [self sendDataToServer:@"back"];
}


-(IBAction)helpPressed:(id)sender{
    
    HelpViewController *controller = [[HelpViewController alloc] initWithNibName:@"HelpView" bundle:nil];
    controller.delegate = self;
    controller.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    [self presentModalViewController:controller animated:YES];
    [controller release];
}

- (void)helpViewControllerDidFinish:(HelpViewController *)controller
{
    [self dismissModalViewControllerAnimated:YES];
}

-(void)addButtonToToolBar{
    
    NSMutableArray *newItems = [toolbar.items mutableCopy];
    
    // for back button
    
    UIImage *backImage = [UIImage imageNamed:@"IphoneNavigationButton_Back.png"];
    toolbarBackButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [toolbarBackButton setHidden:TRUE];
    [toolbarBackButton setTitle:@"Back" forState:UIControlStateNormal];
    [toolbarBackButton setImage:backImage forState:UIControlStateNormal];
    toolbarBackButton.frame = CGRectMake(0.0, 0.0, backImage.size.width, backImage.size.height);
    [toolbarBackButton addTarget:self action:@selector(backButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *backBarButtonItem = [[[UIBarButtonItem alloc] initWithCustomView:toolbarBackButton]autorelease];
    
    [newItems insertObject:backBarButtonItem atIndex:0];
    
    
    
    toolBarLable = [[UILabel alloc] initWithFrame:CGRectMake(0.0 , 20.0f, 180, 44.0f)];
    [toolBarLable setFont:[UIFont fontWithName:@"Helvetica-Bold" size:15]];
    [toolBarLable setBackgroundColor:[UIColor clearColor]];
    [toolBarLable setTextColor:[UIColor colorWithRed:255.0/255.0 green:255.0/255.0 blue:255.0/255.0 alpha:1.0]];
    [toolBarLable setText:@"Accounter"];
    [toolBarLable setTextAlignment:UITextAlignmentCenter];
    toolBarLable.shadowColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
    toolBarLable.shadowOffset = CGSizeMake(0, -1.0);
    
    UIBarButtonItem *title = [[[UIBarButtonItem alloc] initWithCustomView:toolBarLable]autorelease];
    [newItems insertObject:title atIndex:2];    
    
    //for cancel button
    
    UIImage *buttonImage = [UIImage imageNamed:@"IphoneNavigationButton_Button.png"];
    toolBarCancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [toolBarCancelButton setHidden:TRUE];
    [toolBarCancelButton setTitle:@"Back" forState:UIControlStateNormal];
    [toolBarCancelButton setImage:buttonImage forState:UIControlStateNormal];
    toolBarCancelButton.frame = CGRectMake(0.0, 0.0, buttonImage.size.width, buttonImage.size.height);
    [toolBarCancelButton addTarget:self action:@selector(cancelPressed:) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *cancelBarButtonItem = [[[UIBarButtonItem alloc] initWithCustomView:toolBarCancelButton]autorelease];
    
    [newItems insertObject:cancelBarButtonItem atIndex:4];
    
    
    toolbar.items = newItems;
}


-(void)sendCookieToServer{
    NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    NSString *cookieString = [prefs stringForKey:@"accounterCookie"];
    
    NSString *language = [[NSLocale preferredLanguages]objectAtIndex:0];
    
    // NSLog(@"languege : %@",language);
    
    if(cookieString != nil){
        //NSLog(@"cookie got : %@",cookieString);
        NSString *cookieNlanguage = [[cookieString stringByAppendingString:@" "]stringByAppendingString:language];
        [self sendDataToServer:cookieNlanguage];
        sendInitializeStream = TRUE;
    }else{
        NSString *cookieNlanguage = [[@"accounter" stringByAppendingString:@" "]stringByAppendingString:language];
        [self sendDataToServer:cookieNlanguage];
        sendInitializeStream = TRUE;
    }
}

#pragma mark -
#pragma mark Handle Server connection

- (void)initServerCommunication {
    
    applicationConnected = TRUE;
    
    //NSLog(@"connecting to : %@",mainServerAddress);
    
    CFReadStreamRef readStream;
    CFWriteStreamRef writeStream;
    
    CFStreamCreatePairWithSocketToHost(NULL, (CFStringRef)mainServerAddress, 9084, &readStream, &writeStream);
    
    if (readStream && writeStream)
    {
        CFReadStreamSetProperty(readStream, kCFStreamPropertyShouldCloseNativeSocket, kCFBooleanTrue);
        CFWriteStreamSetProperty(writeStream, kCFStreamPropertyShouldCloseNativeSocket, kCFBooleanTrue);
        
        inputStream = (NSInputStream *)readStream;
        outputStream = (NSOutputStream *)writeStream;
        
        [inputStream retain];
        [outputStream retain];
        
        [inputStream setDelegate:self];
        [outputStream setDelegate:self];
        
        [inputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
        [outputStream scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
        
        [inputStream open];
        [outputStream open];
        
        [inputStream setProperty:NSStreamSocketSecurityLevelNegotiatedSSL
                          forKey:NSStreamSocketSecurityLevelKey];
        [outputStream setProperty:NSStreamSocketSecurityLevelNegotiatedSSL
                           forKey:NSStreamSocketSecurityLevelKey];
        
        
    }
    
    //    // check if a pathway to a random host exists
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(checkNetworkStatus:) name:kReachabilityChangedNotification object:nil];
    internetReachable = [[Reachability reachabilityForInternetConnection] retain];
    [internetReachable startNotifier];
    hostReachable = [[Reachability reachabilityWithHostName: @"www.accounterlive.com"] retain];
    [hostReachable startNotifier];
    
}


- (void)stream:(NSStream *)theStream handleEvent:(NSStreamEvent)streamEvent {
    
    
    
	switch (streamEvent) {
		case NSStreamEventOpenCompleted:          
            
            NSLog(@"stream open");
            startTimer = TRUE;
            [self serverConnected];
            break;
            
		case NSStreamEventHasBytesAvailable:{
            NSLog(@"bytes available");
            
            while ([inputStream hasBytesAvailable]) {
                
                NSMutableData *_data = [[[NSMutableData data] retain]autorelease];
                uint8_t buf[1024*5];
                int len = 0;
                
                len = [inputStream read:buf maxLength:sizeof(buf)];
                
                if (len > 0) {
                    
                    [_data initWithBytes:(const void *)buf length:len];
                    [_data replaceBytesInRange:NSMakeRange(0, 4) withBytes:NULL length:0];
                    
                    NSString *output = [[NSString alloc]initWithData:_data encoding:NSUTF8StringEncoding];
                    
                   // NSLog(@"output : %@",output);
                    
                    if (nil != output) {
                        id object = [parser objectWithString:output];
                        // NSLog(@"error : %@",[parser error]);
                        [jsonDeserializer setObject:object];
                        [self resetControls];
                        [self CreateControls:[jsonDeserializer newDeserializer]];
                    }
                }
            }
        }
			break;			
		case NSStreamEventErrorOccurred:
			NSLog(@"Can not connect to the host!");
            startTimer = FALSE;
            applicationConnected = FALSE;
            
            [self serverDisconnected];
            
            [theStream close];
            sendInitializeStream = FALSE;
            [inputStream close];
            [outputStream close];
			break;
		case NSStreamEventEndEncountered:
            NSLog(@"Event end encountered");
			break;
		default:{
            //            if(sendInitializeStream == FALSE){
            //                [self sendCookieToServer];
            //            }
            break;
        }    
    }
}




#pragma mark-
#pragma mark check network

- (void) checkNetworkStatus:(NSNotification *)noticeigig
{
    
    NetworkStatus internetStatus = [internetReachable currentReachabilityStatus];
    switch (internetStatus)
    
    {
        case NotReachable:
        {
            // [self serverDisconnected];
            break;   
        }
        case ReachableViaWiFi:
        {
            
            if(applicationConnected == FALSE){
                
                [self serverConnected];
                [self initServerCommunication];
                [self sendCookieToServer];
                
            }
            break;
        }
        case ReachableViaWWAN:
        {
            if(applicationConnected == FALSE){
                [self serverConnected];
                [self initServerCommunication];
                [self sendCookieToServer];
            }
            break;
        }
            
    }
    
    /*  NetworkStatus hostStatus = [hostReachable currentReachabilityStatus];
     
     switch (hostStatus)
     {
     case NotReachable:
     {
     NSLog(@"host unreach");
     break;
     }
     case ReachableViaWiFi:
     {
     NSLog(@"host reachable");
     break;
     }
     case ReachableViaWWAN:
     
     NSLog(@"host reachable");
     break;
     }*/
    
}



#pragma mark-
#pragma mark Messages sent to server


-(IBAction)sendActionMessage:(id)sender{
    
    for (NSString *s in [commandCode allKeys]) {
        if ([s isEqualToString:[sender titleForState:UIControlStateNormal]]) {
            [self sendDataToServer:[commandCode valueForKey:s]];
            break;
        }
    }    
    //[self resetControls];
}

- (void)sendTextMessage:(NSString *)value{
    
    [self sendDataToServer:value];
    
}

-(IBAction)sendResultListMessage:(id)sender{
    
    for (NSString *s in [listCode allKeys]) {
        if ([s isEqualToString:sender]) {
            [self sendDataToServer:[listCode valueForKey:s]];
            break;
        }
    }   
    
}

-(IBAction)sendCommandListMessage:(id)sender{
    
    for (NSString *s in [commandCode allKeys]) {
        if ([s isEqualToString:sender]) {
            [self sendDataToServer:[commandCode valueForKey:s]];
            break;
        }
    }   
}


-(void)sendDataToServer:(NSString *)value{
    
    NSString *response  = [[NSString alloc ]initWithString:value];
    // NSLog(@"send to server : %@",value);
    int lendata = [response length];
    
    uint32_t newlen=CFSwapInt32HostToBig(lendata);
    NSMutableData *data = [[NSMutableData alloc]initWithBytes:&newlen length: sizeof(newlen)];
    [data appendData:[response dataUsingEncoding:NSUTF8StringEncoding]];
    [outputStream write:[data bytes] maxLength:[data length]];
    [data release];
    [response release];
}



#pragma mark -
#pragma mark Create Controls

-(void)CreateControls :(id)mainResultObject{
    
    BOOL createTextField = FALSE;
    
    [listCode removeAllObjects];
    [commandCode removeAllObjects];
    
    
    NSMutableArray *resultArray = [[NSMutableArray alloc]initWithArray:[mainResultObject getResultParts]];
    
    InputType *inputType;
    
    for (int i=0; i< [resultArray count]; i++) {
        
        id value = [resultArray objectAtIndex:i];
        if ([[value class]isSubclassOfClass:[NSString class]]) {
            
            [self addLabel:value ];
            
        }else if([[value class]isSubclassOfClass:[ResultList class]]){
            
            [cellTableArray removeAllObjects];
            
            ResultList *resultList = [[ResultList alloc]init];
            resultList = value;   
            
            if ([resultList getTitle]!=nil) {
                [self addResultListTitle:[resultList getTitle]];
            }
            NSMutableArray * recordsArray = [[NSMutableArray alloc]initWithArray:[resultList getRecords]];
            
            for (int i = 0; i< [recordsArray count]; i++) {
                
                Record *record = [[[Record alloc]init]autorelease];
                record = [recordsArray objectAtIndex:i];
                
                NSMutableArray * cellsArray = [[NSMutableArray alloc]initWithArray:[record getCells]];
                
                [cellTableArray addObject:record];
                
                Cell *nameCell = [[[Cell alloc]init]autorelease];
                nameCell = [cellsArray objectAtIndex:0];
                if([[nameCell getValue]length]>0){
                    [listCode setValue:[record getCode] forKey:[nameCell getValue]];
                }
                else{
                    [listCode setValue:[record getCode] forKey:[nameCell getName]];
                }
                
            }
            [self addResultTable];

        }else if([[value class]isSubclassOfClass:[CommandList class]]){
            [commandTableArray removeAllObjects];
            
            //this one is required to send the code to send to server
            CommandList *commandList = [[CommandList alloc]init];
            commandList = value;
            NSMutableArray *commandNames = [[NSMutableArray alloc]initWithArray:[commandList getCommandNames]];
            
            for (int i =0; i< [commandNames count]; i++) {
                
                Command * command = [[Command alloc]init];
                command = [commandNames objectAtIndex:i];
                
                [commandCode setValue:[command getCode] forKey:[command getName]];
                [commandTableArray addObject:[command getName]];

            }
            
            [self addCommandTable];
            
        } else if([[value class]isSubclassOfClass:[InputType class]]){
            
            inputType = [[InputType alloc]init];
            inputType = value;
            
            if([inputType getInputType]==8){
                
                NSDateFormatter *dtf = [[NSDateFormatter alloc]init];
                [dtf setDateFormat:@"yyyyMMdd"];
                
                NSDate *newDate = [[NSDate alloc]init];
                newDate = [dtf dateFromString:[inputType getTextValue]];
                
                NSDateFormatter *dateFormatter = [[NSDateFormatter alloc]init];
                [dateFormatter setDateStyle:NSDateFormatterLongStyle];
                [dateFormatter setTimeStyle:NSDateFormatterNoStyle];
                
                NSString *str = [[NSString alloc]initWithString:[dateFormatter stringFromDate:newDate]];
                
                textFieldValue = str;
                
                
            }else{
                textFieldValue = [inputType getTextValue];
            }
            
            if([inputType getInputType] == 0){
                
            }else{
                createTextField = TRUE;
                keyboardType = [inputType getKeyboardType];    
            }
            [inputType release];
        }
    }
    
    
    if (createTextField == TRUE) {
        [self addTextField:[inputType getInputType]];
        createTextField = FALSE;
        [commandTextField becomeFirstResponder];
    }
    
    [self otherControlsManage:mainResultObject];
    [self addHelpButton];
    
    
    scrollview.contentSize = CGSizeMake(scrollview.frame.size.width,yPosition);
    [scrollview setScrollsToTop:TRUE];
    
    
    
}

-(void)addHelpButton{
    
    helpButton = [UIButton buttonWithType:UIButtonTypeInfoDark];
    [helpButton addTarget:self action:@selector(helpPressed:) forControlEvents:UIControlEventTouchUpInside];
    [helpButton setFrame:CGRectMake(screenWidth-20, yPosition, 18, 18)];
    yPosition = yPosition+20;
    [scrollview addSubview:helpButton];  
}


- (void)otherControlsManage:(id)newresult{
    Result* result = [[Result alloc]init];
    result = newresult;
    
    [toolbarBackButton setHidden:[result showBackValue]];
    [toolBarCancelButton setHidden:[result hideCancelValue]];
    
    
    if([[result getTitle]length]>0)
        [toolBarLable setText:[result getTitle]];
    else
        [toolBarLable setText:@"Accounter"];
    
}

-(void)addLabel:(NSString *)title{
    
    int height = 25;
    
    resultLabel = [[UITextView alloc] initWithFrame:CGRectMake(10, yPosition, screenWidth-10, height)];
    [resultLabel setEditable:FALSE];
    [resultLabel setTextAlignment:UITextAlignmentLeft];
    [resultLabel setBackgroundColor:[UIColor clearColor]];
    [resultLabel setScrollEnabled:NO];
    
    [resultLabel setTextColor:[UIColor blackColor]];
    resultLabel.font = [UIFont boldSystemFontOfSize:13];
    
    
    if([[resultLabel text]length]>0){
        
        NSString *lableText = [[NSString alloc]initWithString:[[[resultLabel text] stringByAppendingString:@"\n"]stringByAppendingString:title]];
        [resultLabel setText:lableText];
        
    }else{
        [resultLabel setText:title];
    }
    
    
    [scrollview addSubview:resultLabel];
    
    yPosition = yPosition + height;
    
}
-(void)addResultListTitle:(NSString *)title{
    int height = 25;
    
    resultLisTitle = [[UILabel alloc] initWithFrame:CGRectMake(10, yPosition, screenWidth-10, height)];
    [resultLisTitle setTextAlignment:UITextAlignmentLeft];
    [resultLisTitle setBackgroundColor:[UIColor clearColor]];
    resultLisTitle.lineBreakMode = UILineBreakModeWordWrap;
    resultLisTitle.numberOfLines = 0;
    [resultLisTitle setTextColor:[UIColor blackColor]];
    resultLisTitle.font = [UIFont boldSystemFontOfSize:13];
    
    NSString *lableText = [[NSString alloc]initWithString:title];
    [resultLisTitle setText:lableText];
    
    [scrollview addSubview:resultLisTitle];
    
    yPosition = yPosition + height;
}


-(void)addResultTable{
    
    Record *record = [[[Record alloc]init]autorelease];
    record = [cellTableArray objectAtIndex:0];
    int heit = [[record getCells]count];

    float listHeight = 0.0f;
    
    if(heit<2){
        listHeight = [cellTableArray count] *(heit*35.0);
    }else{
        listHeight = [cellTableArray count] *(heit*25.0);
    }

    ResultTableController *resultTable = [[ResultTableController alloc]initWithFrame:CGRectMake(0,yPosition,screenWidth,listHeight)];
    [resultTable setCellTableArray:cellTableArray];
    [resultTable createTable:self];
    yPosition = yPosition+listHeight;
    
    [scrollview addSubview:resultTable];
    
    
}

-(void)addCommandTable{
    
    float listHeight = 44.0 * [commandTableArray count];
    
    CommandTableController *commandListTable = [[CommandTableController alloc]initWithFrame:CGRectMake(0,yPosition,screenWidth,listHeight)];
    [commandListTable createTable:commandTableArray :self];
    
    yPosition = yPosition+listHeight;
    
    [scrollview addSubview:commandListTable];
    
}


-(void)addTextField :(int)inputType{
    UIKeyboardType keyType = keyboardType;
    
    commandTextField = [[UITextField alloc]initWithFrame:CGRectMake(0,yPosition,screenWidth,44)];
    [commandTextField setDelegate:self];
    //[commandTextField setBarStyle:UIBarStyleDefault];
    [commandTextField setKeyboardType:keyType];
    [commandTextField setPlaceholder:@"Enter your command here"];
    [commandTextField setBackgroundColor:[UIColor clearColor]];
    //[[commandTextField.subviews objectAtIndex:0]removeFromSuperview];
    if(inputType==4){
        //Password
        commandTextField.secureTextEntry = YES;
    }
    
    for (UIView *searchBarSubview in [commandTextField subviews]) {
        
        if([searchBarSubview isKindOfClass:[UITextField class]]){
            UITextField *textField = (UITextField*)searchBarSubview;
            textField.leftView = nil;
            [textField selectAll:textField];
        }
        
        if ([searchBarSubview conformsToProtocol:@protocol(UITextInputTraits)]) {
            [(UITextField *)searchBarSubview setReturnKeyType:UIReturnKeySend];
            [(UITextField *)searchBarSubview setKeyboardAppearance:UIKeyboardAppearanceAlert];
        }
    }
    
    if([textFieldValue length]>0){
        [commandTextField setText:textFieldValue];
    }
    
    [scrollview addSubview:commandTextField];
    yPosition = yPosition +44;
    
}

-(void)addDatePicker{
    
    datePicker = [[UIDatePicker alloc]initWithFrame:CGRectMake(0, 200, 360, 300)];
    NSDate *now = [NSDate date];
    [datePicker setDatePickerMode:UIDatePickerModeDate];
    [datePicker addTarget:self action:@selector(dateSelected:) forControlEvents:UIControlEventValueChanged];
    [datePicker setDate:now animated:YES];
    
    
    CATransition *animation = [CATransition animation];
    [animation setDelegate:self];
    // Set the type and if appropriate direction of the transition, 
    [animation setType:kCATransitionMoveIn];
    [animation setSubtype:kCATransitionFromTop];
    // Set the duration and timing function of the transtion -- duration is passed in as a parameter, use ease in/ease out as the timing function
    [animation setDuration:0.4];
    [animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionLinear]];
    [[datePicker layer] addAnimation:animation forKey:@"transitionViewAnimation"];      
    
    datePicker.hidden = FALSE;
    
    
    [[datePicker layer] removeAnimationForKey:@"transitionViewAnimation"];
    animation = nil;
    
    [scrollview addSubview:datePicker];
    
}
-(void)showDatePicker{
    [datePicker setHidden:TRUE];    
    
}

-(IBAction)dateSelected:(id)sender{
    
    NSDate *selected = [datePicker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc]init];
    [dateFormatter setDateStyle:NSDateFormatterLongStyle];
    
    [commandTextField setText:[dateFormatter stringFromDate:selected]];
    [dateFormatter release];
}
#pragma mark -
#pragma mark keyboard delegates

-(void)textFieldDidBeginEditing:(UITextField *)sender
{
    
    if ([sender isEqual:commandTextField])
    {
        //move the main view, so that the keyboard does not hide it.
        if  (yPosition >= scrollview.frame.size.height)
        {
            // [self setViewMovedUp:YES];
        }
    }
}

//method to move the view up/down whenever the keyboard is shown/dismissed
-(void)setViewMovedUp:(BOOL)movedUp
{
    [UIView beginAnimations:nil context:NULL];
    
    [UIView setAnimationDuration:0.5]; // if you want to slide up the view
    
    CGRect rect = scrollview.frame;
    
    // scrollview.contentSize = CGSizeMake(mainView.frame.size.width,mainView.frame.size.height);
    
    if (movedUp)
    {
        // 1. move the view's origin up so that the text field that will be hidden come above the keyboard 
        // 2. increase the size of the view so that the area behind the keyboard is covered up.
        rect.origin.y -= kOFFSET_FOR_KEYBOARD;
        rect.size.height += kOFFSET_FOR_KEYBOARD;
        
    }
    else
    {
        // revert back to the normal state.
        rect.origin.y += kOFFSET_FOR_KEYBOARD;
        rect.size.height -= kOFFSET_FOR_KEYBOARD;
        
        // scrollview.contentSize = CGSizeMake(scrollview.frame.size.width,yPosition);
    }
    commandTextField.frame = rect;
    
    [UIView commitAnimations];
}


- (void)keyboardWillShow:(NSNotification *)notif
{
    //keyboard will be shown now. depending for which textfield is active, move up or move down the view appropriately
    
    if ([commandTextField isFirstResponder] && commandTextField.frame.origin.y >= 0)
    {
        //[self setViewMovedUp:YES];
    }
    else if (![commandTextField isFirstResponder] && commandTextField.frame.origin.y < 0)
    {
        // [self setViewMovedUp:NO];
    }
}


- (void)viewWillAppear:(BOOL)animated
{
    // register for keyboard notifications
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) 
                                                 name:UIKeyboardWillShowNotification object:self.view.window]; 
}

- (void)viewWillDisappear:(BOOL)animated
{
    // unregister for keyboard notifications while not visible.
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil]; 
}


#pragma mark - Search bar delegates 


- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar {
    
    [searchBar setShowsCancelButton:YES animated:TRUE];
    
    NSString *buttonTitle;
    
    if ([searchBar keyboardType] == UIKeyboardTypeDecimalPad) {
        
        [searchBar resignFirstResponder];
        buttonTitle = [[NSString alloc]initWithString:@"Done"];
        if(datePickerShown == FALSE){
            [self addDatePicker];
            datePickerShown = TRUE;
        }
    }
    else{
        buttonTitle = [[[NSString alloc]initWithString:@"Close"]autorelease];        
    }
    
    for (UIView *searchBarSubview in [searchBar subviews]) {
        if([searchBarSubview isKindOfClass:[UIButton class]]){
            UIButton *but = (UIButton*)searchBarSubview;
            [but setTitle:buttonTitle forState:UIControlStateNormal];
        }
    }
    
}
- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    
    if ([searchBar keyboardType] == UIKeyboardTypeDecimalPad) {
        [self showDatePicker];
        
        //        NSTimeInterval nowTimeSince1970 = [[datePicker date] timeIntervalSince1970];
        //        NSDate *newDate = [[NSDate alloc]initWithTimeIntervalSince1970:nowTimeSince1970];
        //        
        //        
        //        NSLog(@"new date : %@",[newDate description]);
        //        NSString *newStr = [NSString stringWithFormat:@"%f", nowTimeSince1970*1000];
        //        NSRange range = [newStr rangeOfString:@"."];
        //        NSString *substring = [newStr substringToIndex:NSMaxRange(range)];
        //        
        //        substring = [substring substringToIndex:[substring length]-1];
        
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc]init];
        [dateFormatter setDateFormat:@"yyyyMMdd"];
        NSString *str = [[NSString alloc]initWithString:[dateFormatter stringFromDate:[datePicker date]]];
        [self sendTextMessage:str];
        [datePicker removeFromSuperview];
        datePickerShown = FALSE;
        
        [dateFormatter release];
    }else{
        [searchBar resignFirstResponder];
        [searchBar setText:@""];
        
    }
    [searchBar setShowsCancelButton:NO animated:TRUE];
    
    //[self setViewMovedUp:NO];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    
    [searchBar resignFirstResponder];
    [self sendTextMessage:[searchBar text]];
    [searchBar setText:@""];
    [searchBar setShowsCancelButton:NO animated:TRUE];
    
}


//TextBox Deligates


- (BOOL) textFieldShouldReturn:(UITextField *)textField {
    if(textField == commandTextField){
        [textField resignFirstResponder];
        [textField resignFirstResponder];
        [self sendTextMessage:[textField text]];
        [textField setText:@""];
    }
    return YES;
}

- (void) textFieldTextDidBeginEditing:(UISearchBar *)textField {
    
    [textField setShowsCancelButton:YES animated:TRUE];
    
    NSString *buttonTitle;
    
    if ([textField keyboardType] == UIKeyboardTypeDecimalPad) {
        [textField resignFirstResponder];
        buttonTitle = [[NSString alloc]initWithString:@"Done"];
        if(datePickerShown == FALSE){
            [self addDatePicker];
            datePickerShown = TRUE;
        }
    }
    else{
        buttonTitle = [[[NSString alloc]initWithString:@"Close"]autorelease];
    }
    
    for (UIView *searchBarSubview in [textField subviews]) {
        if([searchBarSubview isKindOfClass:[UIButton class]]){
            UIButton *but = (UIButton*)searchBarSubview;
            [but setTitle:buttonTitle forState:UIControlStateNormal];
        }
    }
    
}


@end
