//
//  ResultList.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface ResultList : NSObject {
    
    NSMutableArray *records;
    BOOL isMultiSelection;
    NSString *name;
    NSString *title;
    
}

- (void)setRecords:(NSMutableArray *)recordsList;
- (void)setMutiSelection:(BOOL)value;
- (void)setName:(NSString *)newName;

- (NSString *)getName;
- (NSMutableArray *)getRecords;
- (BOOL)isMultiSelectionEnable;

- (void)setTitle:(NSString *)newTitle;
- (NSString *)getTitle;


@end
