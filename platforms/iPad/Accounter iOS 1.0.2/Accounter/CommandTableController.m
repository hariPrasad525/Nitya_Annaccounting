//
//  CommandTableController.m
//  Accounter
//
//  Created by Amrit Mishra on 11/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "CommandTableController.h"


@implementation CommandTableController

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        width = self.frame.size.width;
        height = self.frame.size.height;
    }
    return self;
}


- (void)dealloc
{
    [super dealloc];
}

-(void)createTable:(NSMutableArray*)nameArray :(id)object{
    
    tableDataArray = [[NSMutableArray alloc]initWithArray:nameArray];   
    callingClass = object;
    
    commandListTable = [[UITableView alloc] initWithFrame:CGRectMake(0,0,width,height) style:UITableViewStylePlain];
    [commandListTable setDelegate:self];
    commandListTable.tableHeaderView = nil;
    commandListTable.tableFooterView =nil;
    [commandListTable setDataSource:self];
    [commandListTable setSeparatorColor:[[UIColor alloc]initWithRed:224/255.0 green:224/255.0 blue:224/255.0 alpha:1.0]];
    commandListTable.editing = NO;
    [commandListTable setScrollEnabled:FALSE];
    
    [self addSubview:commandListTable];
}


#pragma mark -
#pragma mark Table view methods


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
     return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return [tableDataArray count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
 
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell* cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    
    int red = 0;
    int green = 139;
    int blue = 139;
    
    CGFloat redColor = (CGFloat)(red+1)/256;
    CGFloat greenColor = (CGFloat)(green+1)/256;
    CGFloat blueColor = (CGFloat)(blue+1)/256;

    
    cell.textLabel.text = [tableDataArray objectAtIndex:indexPath.row];
    [cell.textLabel setFont:[UIFont boldSystemFontOfSize:14]];
    cell.textLabel.textColor = [[UIColor alloc]initWithRed:redColor green:greenColor blue:blueColor alpha:1.0];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [callingClass sendCommandListMessage:[tableDataArray objectAtIndex:indexPath.row]];
    
}


@end
