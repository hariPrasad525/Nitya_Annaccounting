//
//  ResultTableController.h
//  Accounter
//
//  Created by Amrit Mishra on 11/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Cell.h"
#import "Record.h"

@interface ResultTableController : UIView< UITableViewDelegate,UITableViewDataSource> {
    
   
    UITableView *resultListTable;

    NSMutableArray *cellTableValue;
    
    NSMutableArray *cells;
    
    id callingClass;
    
    int width;
    int height;
    
}


-(void)setCellTableArray:(NSMutableArray*)cellArray;


-(UILabel*)addCellName:(int)cellWidth:(int)cellHeight:(NSString*)cellText :(int)cellNumber;
-(UILabel*)addCellValue:(int)cellWidth:(int)cellHeight:(NSString*)cellText :(int)cellNumber;


-(void)createTable:(id)object;


@end
