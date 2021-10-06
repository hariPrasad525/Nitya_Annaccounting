//
//  AMenu.h
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/2/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AMenuItem.h"

@interface AMenu : NSObject {
    
    NSMutableArray *menuItems;
    NSString *menuName;
    

}

@property(readwrite,retain)   NSString *menuName;
-(void)setMenuItems:(NSMutableArray*)array;
-(NSMutableArray*)getMenuItems;
-(void)setMenuItem;

@end

