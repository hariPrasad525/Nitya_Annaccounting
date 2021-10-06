//
//  ResultTableController.m
//  Accounter
//
//  Created by Amrit Mishra on 11/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ResultTableController.h"


@implementation ResultTableController

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        width = self.frame.size.width;
        height = self.frame.size.height;
        
    }
    return self;
}

/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect
 {
 // Drawing code
 }
 */

- (void)dealloc
{
    [super dealloc];
}


-(void)setCellTableArray:(NSMutableArray*)cellArray{
    
    cellTableValue = [[NSMutableArray alloc]initWithArray:cellArray];
}


-(void)createTable:(id)object{
    
    callingClass = object;
    
    resultListTable = [[UITableView alloc] initWithFrame:CGRectMake(0,0,width,height) style:UITableViewStylePlain];
    [resultListTable setDelegate:self];
    resultListTable.tableHeaderView = nil;
    [resultListTable setBackgroundColor:[UIColor clearColor]];
    [resultListTable setDataSource:self];
    [resultListTable setSeparatorColor:[[UIColor alloc]initWithRed:224/255.0 green:224/255.0 blue:224/255.0 alpha:1.0]];
    resultListTable.editing = NO;
    [resultListTable setScrollEnabled:FALSE];

    
    [self addSubview:resultListTable];
    
}

-(UILabel*)addCellName:(int)cellWidth:(int)cellHeight:(NSString*)cellText :(int)cellNumber{
    CGRect Label1Frame;
    UIFont *fnt;
    
    Record *record = [[[Record alloc]init]autorelease];
    record = [cellTableValue objectAtIndex:0];
    if([[record getCells] count]<2){
        //This is Single Cell ResultList, So each cell will contains 35 height
       Label1Frame = CGRectMake(10, (cellNumber*25), 120, 35);
    }else{
        //As this is multi cell, each cell will contains 25 height
        Label1Frame = CGRectMake(10, (cellNumber*20), 120, 25);
    }
    switch (cellNumber) {
        case 0:
           
            fnt = [UIFont boldSystemFontOfSize:13];
            break;
      
        default:
            fnt = [UIFont systemFontOfSize:11];
            break;
    }
    
    
    UILabel *lblTemp1;
    
    
    lblTemp1 = [[[UILabel alloc] initWithFrame:Label1Frame]autorelease];
    lblTemp1.tag = 1;
    lblTemp1.font = fnt;
    lblTemp1.textColor = [UIColor blackColor];
    [lblTemp1 setBackgroundColor:[UIColor clearColor]];
    [lblTemp1 setText:cellText];
    
    
    return lblTemp1;
    
    
}

-(UILabel*)addCellValue:(int)cellWidth:(int)cellHeight:(NSString*)cellText :(int)cellNumber{
    
    CGRect Label2Frame;
    
            Label2Frame = CGRectMake(cellWidth-160, (cellNumber*14), 120, 25);
            
       
    UILabel *lblTemp2;
    
    lblTemp2 = [[[UILabel alloc] initWithFrame:Label2Frame]autorelease];
    lblTemp2.tag = 2;
    lblTemp2.font = [UIFont systemFontOfSize:12];
    lblTemp2.textColor = [UIColor grayColor];
    [lblTemp2 setBackgroundColor:[UIColor clearColor]];
    [lblTemp2 setText:cellText];
    
    return lblTemp2;
    
}

#pragma mark -
#pragma mark Table view methods


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [cellTableValue count];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
    return nil;
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell* cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] autorelease];
    
    
    [cell.textLabel setFont:[UIFont boldSystemFontOfSize:14]];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    
    int cWidth = cell.frame.size.width;
    int cHeight = cell.frame.size.height;
    
    
    
    
    Record *record = [[[Record alloc]init]autorelease];
    record = [cellTableValue objectAtIndex:indexPath.row];
    
    NSMutableArray *sarr = [[NSMutableArray alloc]initWithArray:[record getCells]];
    for (int i=0; i<[sarr count]; i++) {
        Cell *nameCell = [[[Cell alloc]init]autorelease];
        nameCell = [sarr objectAtIndex:i];
        [cell.contentView addSubview:[self addCellName:cWidth :cHeight :[nameCell getName] :i]];
        [cell.contentView addSubview:[self addCellValue:cWidth :cHeight :[nameCell getValue] :i]];
    }
    

    return cell;
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    Record *record = [[[Record alloc]init]autorelease];
    record = [cellTableValue objectAtIndex:indexPath.row];
    
    NSMutableArray *sarr = [[NSMutableArray alloc]initWithArray:[record getCells]];
    
    Cell *nameCell = [[[Cell alloc]init]autorelease];
    nameCell = [sarr objectAtIndex:0];
    
      
    if([[[nameCell getValue]stringByReplacingOccurrencesOfString:@" " withString:@""]length]<1){
        [callingClass sendResultListMessage:[nameCell getName]];
        NSLog(@"sending : %@",[nameCell getName]);
    }
    else{
        [callingClass sendResultListMessage:[nameCell getValue]];
        NSLog(@"sending : %@",[nameCell getValue]);
    }
    
    
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    Record *record = [[[Record alloc]init]autorelease];
    record = [cellTableValue objectAtIndex:indexPath.row];
    if([[record getCells]count]<2){
        return 35;
    }else{
        return [[record getCells]count]*25;
    }
}


@end
