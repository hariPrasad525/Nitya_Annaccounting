//
//  CommandTableController.h
//  Accounter
//
//  Created by Amrit Mishra on 11/22/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface CommandTableController : UIView< UITableViewDelegate,UITableViewDataSource>  {
    
    
    UITableView *commandListTable;
    NSMutableArray *tableDataArray;
    
    int width;
    int height;
    
        id callingClass;
    
}
-(void)createTable:(NSMutableArray*)nameArray:(id)object;


@end
