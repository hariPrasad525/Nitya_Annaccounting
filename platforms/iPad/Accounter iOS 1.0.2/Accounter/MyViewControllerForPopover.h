//
//  MyViewControllerForPopover.h
//  My
//
//  Created by duivesteyn.net on 13.04.10.
//  Copyright 2010 de. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AMenuItem.h"
#import "AMenu.h"

@interface MyViewControllerForPopover : UIViewController {
    
    
    IBOutlet UIToolbar *toolBar;
    NSMutableArray *menuItems;
    NSMutableArray *objectArray;
    IBOutlet UITableView *tableView;
    NSMutableArray *oldArray;
    NSMutableArray *indicatorArray;
    id mainObject;
    id uiObject;
    
    NSString *logoutUrl;
    NSString *companiesURL;
    
}
-(void)setObject:(id)object:(id)uiobject;
-(void)setMenuItemsAray:(NSMutableArray*)items;
-(void)resetMenu;
-(void)addBackButton;
-(IBAction)goBack:(id)sender;
-(void)changeUrl:(NSString*)url;
-(void)setResetUrl:(NSString*)logouturl:(NSString*)companiesUrl;
-(IBAction)close:(id)sender;

@end
