//
//  MyViewControllerForPopover.m
//  My
//
//  Created by duivesteyn.net on 13.04.10.
//  Copyright 2010 de. All rights reserved.
//

#import "MyViewControllerForPopover.h"


@implementation MyViewControllerForPopover

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
 - (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
 if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
 // Custom initialization
 }
 return self;
 }
 */

-(void)viewDidAppear:(BOOL)animated 
{
    
	self.contentSizeForViewInPopover = CGSizeMake(250,350);     // size popover to what you wish, this may change yet aggain?
	
}

/*
 // Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
 - (void)viewDidLoad {
 [super viewDidLoad];
 }
 */

/*
 // Override to allow orientations other than the default portrait orientation.
 - (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
 // Return YES for supported orientations
 return (interfaceOrientation == UIInterfaceOrientationPortrait);
 }
 */

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [tableView release];
    tableView = nil;
    
    [menuItems  removeAllObjects];
    [toolBar release];
    toolBar = nil;
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [tableView release];
    [menuItems release];
    [objectArray release];
    [indicatorArray release];
    [toolBar release];
    [super dealloc];
}
-(void)resetMenu{
    [objectArray removeAllObjects];
    [menuItems removeAllObjects];
    [indicatorArray removeAllObjects];
    
}


-(void)setResetUrl:(NSString*)logouturl:(NSString*)companiesUrl{
    
    logoutUrl = [[NSString alloc]initWithString:logouturl];
    companiesURL = [[NSString alloc]initWithString:companiesUrl];
    
}

-(void)setMenuItemsAray:(NSMutableArray*)items{
    
    NSMutableArray *toolbarButtons = [[NSMutableArray alloc]init];
    UIBarButtonItem *close = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(close:)];
    UIBarButtonItem *spacer1 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    [toolbarButtons addObject:spacer1];
    [toolbarButtons addObject:close];
    [toolBar setItems:toolbarButtons ];
    
    menuItems = [[NSMutableArray alloc]init];
    objectArray = [[NSMutableArray alloc]initWithArray:items];
    indicatorArray = [[NSMutableArray alloc]init];
    
    for (NSObject *object in items) {
        
        if([[object class]isSubclassOfClass:[AMenu class]]){
            [menuItems addObject:[(AMenu*)object menuName]];
            [indicatorArray addObject:@"1"];
            
        }else if([[object class]isSubclassOfClass:[AMenuItem class]]){
            [menuItems addObject:[(AMenuItem*)object menuName]];
            [indicatorArray addObject:@"0"];
            
        }
    }
    [tableView reloadData];
}
-(void)setObject:(id)object :(id)uiobject{
    mainObject = object;  
    uiObject = uiobject;
}

#pragma mark -
#pragma mark Table view methods


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return [menuItems count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell* cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    cell.textLabel.text = [menuItems  objectAtIndex:indexPath.row];
    [cell.textLabel setFont:[UIFont boldSystemFontOfSize:12]];
    
    if([[indicatorArray  objectAtIndex:indexPath.row]isEqual:@"1"]){
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }else{
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
      if([[[objectArray objectAtIndex:indexPath.row]class]isSubclassOfClass:[AMenu class]]){
        NSMutableArray *items = [[NSMutableArray alloc]initWithArray:[[objectArray objectAtIndex:indexPath.row] getMenuItems]];
        oldArray = [[NSMutableArray alloc]initWithArray:objectArray];
        [self setMenuItemsAray:items];
        [self addBackButton];
        
    }else if([[[objectArray objectAtIndex:indexPath.row]class]isSubclassOfClass:[AMenuItem class]]){
        [self changeUrl:[(AMenuItem*)[objectArray objectAtIndex:indexPath.row]menuURL]];
    }
    
}
-(void)addBackButton{
    
    UIBarButtonItem *title = [[UIBarButtonItem alloc]initWithTitle:@"Back"style:UIBarButtonItemStyleBordered target:self action:@selector(goBack:)]; 
    UIBarButtonItem *spacer1 = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    UIBarButtonItem *close = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(close:)];
    
    NSMutableArray *toolbarButtons = [[NSMutableArray alloc]init];
    [toolbarButtons addObject:title];
    [toolbarButtons addObject:spacer1];
    [toolbarButtons addObject:close];
    [toolBar setItems:toolbarButtons ];
    
}

-(IBAction)goBack:(id)sender{
    
    [self setMenuItemsAray:oldArray];
}

-(void)changeUrl:(NSString*)url{
    if(([url isEqualToString:logoutUrl])||([url isEqualToString:@"main/companies"])){
        [uiObject resetMainMenu];
    }
    [uiObject hidePopup];
    [mainObject changeBrowserUrl:url];
}

-(IBAction)close:(id)sender{
    [uiObject hidePopup];
}
@end
