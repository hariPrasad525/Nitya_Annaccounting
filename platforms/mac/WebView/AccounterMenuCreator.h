//
//  AccounterMacMenu.h
//  Accounter
//
//  Created by Amrit Mishra on 12/12/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface AccounterMenuCreator : NSObject<NSXMLParserDelegate> {
@private
    
    NSString *filePath;  
    
    
    NSMutableDictionary *menuLinksDictonary;
    //    NSString *menuTitle;
    //    NSString *menuURL;
    
    NSMenu *_accounterMenu;
    NSMenu *_menuBar;

    
    int noOfMainMenusInserted;
    int noOfAccounterMenuItemsInserted;
    
    NSMenuItem *menuItem; 

    NSMutableArray *menuStack;
    
    id _mainClass;
}

-(AccounterMenuCreator*)initWith:(NSString*)xmlFilePath:(NSMenu*)mainMenu: (NSMenu*)logoutMenu :(id)mainClass;
-(void)create;
-(void)clear;
- (IBAction)sendUrl:(id)sender event:(NSMenuItem*)event;


@end
