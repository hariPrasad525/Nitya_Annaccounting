//
//  GradientView.m
//  Accounter iOS2
//
//  Created by Amrit Mishra on 11/15/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GradientView.h"


@implementation GradientView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)drawRect:(CGRect)rect {
	
	//making CGContext
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	CGGradientRef gradient;
	CGColorSpaceRef colorSpace;
	
	size_t num_locations = 2;
	CGFloat locations[2] = { 0.0, 1.0 };
    
    
    int red = 3;
    int green = 168;
    int blue = 158;
    
    CGFloat redColor = (CGFloat)(red+1)/256;
    CGFloat greenColor = (CGFloat)(green+1)/256;
    CGFloat blueColor = (CGFloat)(blue+1)/256;
    
    int red1 = 0;
    int green1 = 120;
    int blue1 = 120;
    
    CGFloat redColor1 = (CGFloat)(red1+1)/256;
    CGFloat greenColor1 = (CGFloat)(green1+1)/256;
    CGFloat blueColor1 = (CGFloat)(blue1+1)/256;
    
    
	CGFloat components[12] = { redColor,blueColor,greenColor,1.0,redColor1,blueColor1,greenColor1,1.0}; // End color
	
	colorSpace = CGColorSpaceCreateDeviceRGB();
	gradient = CGGradientCreateWithColorComponents(colorSpace, components,locations, num_locations);
	
	CGPoint startPoint = CGPointMake(self.frame.size.width/2,0.0);//left side of rect
	CGPoint endPoint = CGPointMake(self.frame.size.width/2, self.frame.size.height);//right side of rect
	
	CGContextDrawLinearGradient(context, gradient, startPoint, endPoint, 0);
	
	//release
	CGColorSpaceRelease(colorSpace);
	CGGradientRelease(gradient);
}

- (void)dealloc
{
    [super dealloc];
}

@end
