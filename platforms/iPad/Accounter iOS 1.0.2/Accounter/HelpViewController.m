//
//  FlipsideViewController.m
//  FlipTest
//
//  Created by Amrit Mishra on 9/17/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "HelpViewController.h"


@implementation HelpViewController

@synthesize delegate=_delegate;

- (void)dealloc
{
//    [webPage release];
//    [navigationBar release];
//    [toolBar release];
    [mainView release];
    [segmentedButton release];
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    
    webPage  = [[UIWebView alloc]initWithFrame:CGRectMake(0, 0, mainView.frame.size.width, mainView.frame.size.height)];
    [webPage setDelegate:self];
    [mainView addSubview:webPage];

    
    [self addAboutView];
    

}

- (void)viewDidUnload
{

    [mainView release];
    mainView = nil;
    [segmentedButton release];
    segmentedButton = nil;
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Actions

- (IBAction)done:(id)sender
{
    [self.delegate helpViewControllerDidFinish:self];
}


- (IBAction)changeView:(id)sender {
    
    if([segmentedButton selectedSegmentIndex]==0){
        [self addAboutView];
    }else{
        [self addHelpView];
    }
    
}

#pragma mark - Add Views

-(void)addAboutView{
    
      
    NSString     * nsstrPath = [ [ NSBundle mainBundle ] pathForResource : @"about" ofType : @"html" ] ;
    NSURL     * nsURL = [ NSURL fileURLWithPath : nsstrPath ] ;
    NSURLRequest     * nsURLRequest = [ NSURLRequest requestWithURL : nsURL ] ;
    [ webPage loadRequest : nsURLRequest ] ;

}
-(void)addHelpView{
    NSString     * nsstrPath = [ [ NSBundle mainBundle ] pathForResource : @"help" ofType : @"html" ] ;
    NSURL     * nsURL = [ NSURL fileURLWithPath : nsstrPath ] ;
    NSURLRequest     * nsURLRequest = [ NSURLRequest requestWithURL : nsURL ] ;
    [ webPage loadRequest : nsURLRequest ] ;
}
-(void)addBlogView{
    
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType!=UIWebViewNavigationTypeLinkClicked)
    {
        return YES;
    }
    else
    {
        // link in instructions html clicked
        return NO;
    }
}

@end
