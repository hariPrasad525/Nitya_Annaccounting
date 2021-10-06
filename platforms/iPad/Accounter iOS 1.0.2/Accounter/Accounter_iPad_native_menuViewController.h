//
//  Accounter_iPad_native_menuViewController.h
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/2/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AMenu.h"
#import "AMenuItem.h"
#import "MenuCreator.h"
#import "OCPromptView.h"
#import "CacheUpdater.h"
#import "CustomURLProtocol.h"
#import "Reachability.h"

@class Reachability;

@interface Accounter_iPad_native_menuViewController : UIViewController <NSXMLParserDelegate,UIWebViewDelegate,ASIHTTPRequestDelegate>{
    
    AMenu *menu;
    AMenuItem *menuItem;
    NSMutableDictionary *menuLinksDictonary;
    IBOutlet UIView *mainView;

    IBOutlet UIWebView *webBrowser;
    
    NSMutableArray *menuArray;   
    NSMutableArray *accounterMenuArray;
    bool isMainMenu;
    bool isMenuItem;
    IBOutlet UIToolbar *toolBar;
    NSString *mainPathLocation;
    
    OCPromptView *alert;
    
    Reachability* internetReachable;
    Reachability* hostReachable;
}
-(void)startParsing;
-(void)changeBrowserUrl:(NSString*)newUrl;
-(BOOL)getViewTitle:(NSURL*)url;

-(void)isLive:(bool)value;
-(void)connect;

-(void) checkNetworkStatus:(NSNotification *)notice;

@end
