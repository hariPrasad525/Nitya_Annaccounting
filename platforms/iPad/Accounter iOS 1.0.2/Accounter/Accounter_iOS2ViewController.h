//
//  Accounter_iOS2ViewController.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "SBJson.h"
#import "SBJsonParser.h"
#import "ResultJSONDeserializer.h"
#import "OCPromptView.h"
#import "Reachability.h"
#import "GradientView.h"
#import "RoundedUITableView.h"
#import "OCPromptView.h"
#import "ResultTableController.h"
#import "CommandTableController.h"
#import "HelpViewController.h"


@interface Accounter_iOS2ViewController : UIViewController<NSStreamDelegate,UITextFieldDelegate,HelpViewControllerDelegate> {
    
    IBOutlet UINavigationBar *navigationBar;
    IBOutlet UITextField *commandTextField;
    IBOutlet UIView *mainView;
    IBOutlet UIToolbar *toolbar;
    IBOutlet UIActivityIndicatorView *activityIndicator;
    IBOutlet UILabel *activityLabel;
    IBOutlet UIBarButtonItem *toolBarTitle;
    IBOutlet UIButton *helpButton;
    IBOutlet UIDatePicker *datePicker;
    
    NSString *mainServerAddress;
    
    
    UIButton *resultButton;
    NSTimer *loadApplicationTimer;
    NSInputStream *inputStream;
    NSOutputStream *outputStream;
    OCPromptView *alert ;
   // UIAlertView *serverConnectionAlert ;
    UIAlertView *reconnectAlert;
    
    UITextView *resultLabel;
    UILabel *resultLisTitle;
    UIScrollView *scrollview;
    UIButton *toolbarBackButton ;
    UIButton *toolBarCancelButton;
    UILabel *toolBarLable;
    
    ResultJSONDeserializer *jsonDeserializer;
    Reachability* internetReachable;
    Reachability* hostReachable;
    SBJsonParser *parser;
    SBJsonWriter *_writer;
    
    
    NSMutableDictionary *commandCode;
    NSMutableDictionary *listCode;
    

    NSMutableArray *commandTableArray;
    NSString *textFieldValue;
    NSTimer *reconnectionTimer;
    
    
    NSMutableArray *cellTableArray;
    
 //   int interval;
    int attempts;
    int yPosition;
    
    BOOL showingReconnectDialogue;
    BOOL sendInitializeStream;
    BOOL datePickerShown;
    BOOL startTimer;
    CGRect screenRect;
    CGFloat screenWidth;
    CGFloat screenHeight;
    BOOL applicationConnected;
    UIKeyboardType keyboardType;
}


-(IBAction)sendActionMessage:(id)sender ;

-(IBAction)sendResultListMessage:(id)sender ;
-(IBAction)sendCommandListMessage:(id)sender ;
-(IBAction)helpPressed:(id)sender;
-(IBAction)backButtonClicked:(id)sender ;
-(IBAction)cancelPressed:(id)sender;
-(IBAction)cancelPressed:(id)sender;

-(void)checkNetworkStatus:(NSNotification *)notice;
-(void)CreateControls :(id)mainResultObject;
-(void)addLabel:(NSString *)title;
-(void)addResultListTitle:(NSString *)title;
-(void)addResultTable;
-(void)addCommandTable;

//-(void)addButtons:(NSString *)title :(NSString*)code;
-(void)addTextField :(int)inputType;
-(void)otherControlsManage:(id)newresult;
-(void)sendCookieToServer;
-(void)initServerCommunication;
-(void)sendTextMessage:(NSString *)value;
-(void)sendDataToServer:(NSString *)value;
-(void)resetControls;
-(void)initiateVariables;
-(void)setViewMovedUp:(BOOL)movedUp;
-(void)serverDisconnected;
-(void)serverConnected;
-(void)ConnectToMainServer;

-(void)addButtonToToolBar;

-(void)addHelpButton;

-(void)addDatePicker;
-(void)showDatePicker;
-(IBAction)dateSelected:(id)sender;

-(void)timerFired:(NSTimer *) theTimer;


@end
