//
//  AMenu.m
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/2/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "AMenu.h"


@implementation AMenu

@synthesize menuName;

- (id)init {
    self = [super init];
    if (self) {
//        <#initializations#>
    }
    return self;
}
-(void)setMenuItem{
    menuItems = [[NSMutableArray alloc]init];
}
-(void)setMenuItems:(NSMutableArray*)array{
    menuItems = [[NSMutableArray alloc]initWithArray:array];
}

-(NSMutableArray*)getMenuItems{
    return menuItems;
}


@end
