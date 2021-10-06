//
//  Record.h
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/11/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Record : NSObject {
    
    NSMutableArray *cells;
    NSString *code;
}

- (NSString *)getCode;
- (NSMutableArray *)getCells;

- (void)setCells:(NSMutableArray *)cellNames;
- (void)setCode:(NSString *)stringCode;

@end
