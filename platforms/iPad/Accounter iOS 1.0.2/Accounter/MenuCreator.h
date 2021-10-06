//
//  MenuCreator.h
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/3/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AMenu.h"
#import "AMenuItem.h"
#import "MyViewControllerForPopover.h"


@interface MenuCreator : NSObject <UIPopoverControllerDelegate>{
    
    NSMutableArray  *mainMenuArray;
    NSMutableArray *accounterMenuArray;
    NSMutableArray *objectArray;
    UIToolbar *mainToolBar;
    UIView *view;
    UILabel *titleLabel;
    UIPopoverController *popoverController;
    MyViewControllerForPopover  *myViewControllerForPopover;
    
    bool menuVisible;
    UIBarButtonItem *barButtonItem ;
    
}
+(void)initWithMenuArray:(NSMutableArray*)mainMenu :(NSMutableArray*)accounterMenu :(UIToolbar*)toolBarObject :(UIView*)mainView :(id)object;
-(void)createArray:(NSMutableArray*)menuArray:(NSMutableArray*)accounterMenu :(UIToolbar*)toolBarObject :(UIView*)mainView:(id)object;
-(void)addMenu;
-(UIBarButtonItem*)createToolBarItem:(NSObject*)object;
-(void)didChangeSegmentControl:(UISegmentedControl*)control;
-(void)hidePopup;
-(void)changeTitle:(NSString*)title;
-(void)resetMainMenu;
-(IBAction)openHelp:(id)sender;
-(IBAction)openAccounterMenu:(id)sender;

@end
