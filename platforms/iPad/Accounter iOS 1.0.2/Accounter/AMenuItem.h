//
//  AMenuItem.h
//  Accounter iPad native menu
//
//  Created by Amrit Mishra on 1/2/12.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface AMenuItem : NSObject {
    
    NSString *menuName;
    NSString *menuURL;
    
    
}

@property(readwrite,retain)    NSString *menuName;
@property(readwrite,retain)    NSString *menuURL;

@end
