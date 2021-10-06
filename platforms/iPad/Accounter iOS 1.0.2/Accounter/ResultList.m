//
//  ResultList.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "ResultList.h"


@implementation ResultList


-(id)init{
    
  
    
    self = [super init];
    if (self) {
        records = [[NSMutableArray alloc]init];
        title = [[NSString alloc]init];

    }
    return self;
}


- (void)setRecords:(NSMutableArray *)recordsList{
    
    records = [[NSMutableArray alloc]initWithArray:recordsList];
}

- (void)setMutiSelection:(BOOL)value{
    isMultiSelection = value;
}

- (void)setName:(NSString *)newName{
    name = newName;
}

- (NSString *)getName{
    return name;
}

- (NSMutableArray *)getRecords{
    return records;
}

- (BOOL)isMultiSelectionEnable{
    return isMultiSelection;
}

- (void)setTitle:(NSString *)newTitle{
    title = newTitle;
}

- (NSString *)getTitle{
    return title;
}

@end
