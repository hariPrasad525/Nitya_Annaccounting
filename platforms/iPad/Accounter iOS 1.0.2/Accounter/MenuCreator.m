//
//  MenuCreator.m
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/3/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "MenuCreator.h"


@implementation MenuCreator

- (id)init {
    self = [super init];
    if (self) {
        
    }
    return self;
}

+(void)initWithMenuArray:(NSMutableArray*)mainMenu :(NSMutableArray*)accounterMenu :(UIToolbar*)toolBarObject :(UIView*)mainView :(id)object{
    
    [[MenuCreator alloc]createArray:mainMenu:accounterMenu :toolBarObject :mainView:object];
    
}

-(void)createArray:(NSMutableArray*)menuArray:(NSMutableArray*)accounterMenu :(UIToolbar*)toolBarObject :(UIView*)mainView:(id)object{
    
    objectArray = [[NSMutableArray alloc]initWithArray:menuArray];
    mainToolBar = toolBarObject;
    
    view = mainView;
    mainMenuArray =[[NSMutableArray alloc]init];
    
    accounterMenuArray =[[NSMutableArray alloc]initWithArray:accounterMenu];
    
    myViewControllerForPopover = [[MyViewControllerForPopover alloc] initWithNibName:@"MyViewControllerForPopover" bundle:nil ];    
    UIPopoverController *popover = [[UIPopoverController alloc] initWithContentViewController:myViewControllerForPopover];
    popoverController = popover;          // we retain a pointer so we can release later or re-use
    popoverController.delegate = self; 
    [myViewControllerForPopover setObject:object:self];
    
    [self addMenu];
}



-(void)addMenu{
    
    menuVisible = false;
    
    UIBarButtonItem *spacer1 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    UIBarButtonItem *spacer2 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    
    UIBarButtonItem *helpButton = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemBookmarks target:self action:@selector(openHelp:)];    
    [helpButton setStyle:UIBarButtonItemStyleBordered];
    
    
    UIImage *image = [UIImage imageNamed:@"accounter.png"];
    UIBarButtonItem *accounterButton = [[UIBarButtonItem alloc]initWithImage:image style:UIBarButtonItemStylePlain target:self action:@selector(openAccounterMenu:)];    
    
    [mainMenuArray addObject:accounterButton];
    [mainMenuArray addObject:spacer1];
    [mainMenuArray addObject:[self createToolBarItem:@""]];
    [mainMenuArray addObject:spacer2];
    [mainMenuArray addObject:helpButton];
    [mainToolBar setItems:mainMenuArray ];
    
}
-(IBAction)openAccounterMenu:(id)sender{
    [myViewControllerForPopover resetMenu];
    [myViewControllerForPopover setResetUrl:([(AMenuItem*)[accounterMenuArray objectAtIndex:0]menuURL]):([(AMenuItem*)[accounterMenuArray objectAtIndex:1]menuURL])];
    
    [myViewControllerForPopover setMenuItemsAray:accounterMenuArray];
    
    [popoverController presentPopoverFromBarButtonItem:sender permittedArrowDirections:UIPopoverArrowDirectionUp animated:YES];
    
}

-(IBAction)openHelp:(id)sender{
    
    NSURL *url = [NSURL URLWithString:@"http://help.accounterlive.com/"];
    [[UIApplication sharedApplication]openURL:url];
}

-(UIBarButtonItem*)createToolBarItem:(NSObject*)object{
    
    NSMutableArray *att = [[NSMutableArray alloc]init];
    for (NSObject*object in objectArray) {
        if([[object class]isSubclassOfClass:[AMenu class]]){
            [att addObject:[(AMenu*)object menuName]];
        }
    }
    
    UISegmentedControl *segControl = [[UISegmentedControl alloc]initWithItems:att];
    [segControl addTarget:self action:@selector(didChangeSegmentControl:) forControlEvents:UIControlEventValueChanged];
    [segControl setSegmentedControlStyle:UISegmentedControlStyleBar];
    [segControl setTintColor:[UIColor grayColor]];
    [segControl setMomentary:YES];
    barButtonItem = [[UIBarButtonItem alloc] initWithCustomView:segControl];
    
    return barButtonItem;
}

-(void)didChangeSegmentControl:(UISegmentedControl*)control{
    
    NSString *title =[control titleForSegmentAtIndex:control.selectedSegmentIndex];
    [control setTintColor:[UIColor grayColor]];
    
  
    NSMutableArray *itemsArray = [[NSMutableArray alloc]init];
    
    for(int i=0;i<[objectArray count];i++){
        if([[[objectArray objectAtIndex:i]class]isSubclassOfClass:[AMenu class]]){
            if ([title isEqualToString:[(AMenu*)[objectArray objectAtIndex:i]menuName]]) {
                itemsArray = [(AMenu*)[objectArray objectAtIndex:i]getMenuItems];
                
            }
        }
    }
    
    [myViewControllerForPopover resetMenu];
    [myViewControllerForPopover setMenuItemsAray:itemsArray];
    
    int x=[control frame].origin.x;
    x = x + (control.selectedSegmentIndex*80);
    [popoverController presentPopoverFromRect:CGRectMake(x, 10, 10, 10) inView:view permittedArrowDirections:UIPopoverArrowDirectionUp animated:YES];
    
    
}

-(void)resetMainMenu{
    
    [mainMenuArray removeAllObjects];
    [mainToolBar setItems:mainMenuArray ];
}

-(void)changeTitle:(NSString*)title{
    [titleLabel setText:title];
}

-(void)hidePopup{
    
    if([popoverController isPopoverVisible]){
        [popoverController dismissPopoverAnimated:YES];
    }
}


@end
