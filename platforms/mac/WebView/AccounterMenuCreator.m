//
//  AccounterMacMenu.m
//  Accounter
//
//  Created by Amrit Mishra on 12/12/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AccounterMenuCreator.h"


@implementation AccounterMenuCreator

- (id)init
{
    self = [super init];
    if (self) {
        
    }
    
    return self;
}

- (void)dealloc
{
    [filePath release];
    [super dealloc];
}


-(AccounterMenuCreator*)initWith:(NSString*)xmlFilePath:(NSMenu*)menuBar: (NSMenu*)accounterMenu :(id)mainClass{
    
    filePath = [[NSString alloc]initWithString:xmlFilePath];
    
    _menuBar = menuBar;
    menuStack = [[NSMutableArray alloc]init];
    _accounterMenu = accounterMenu;
    
    _mainClass = mainClass;
    noOfMainMenusInserted = 0;
    noOfAccounterMenuItemsInserted=0;
    menuLinksDictonary = [[NSMutableDictionary alloc]init];
    
    return self;
    
}

-(void)create{
    
    [self clear];
    NSURL * versionURL = [NSURL URLWithString:filePath];
    NSXMLParser * parser = [[NSXMLParser alloc] initWithContentsOfURL:versionURL];
    [parser setDelegate:self];
    [parser parse];
    [parser release];
    
    
}

-(void)clear{
    
    for(int i =0;i<noOfMainMenusInserted;i++){
        [_menuBar removeItemAtIndex:1];
    }
    for(int i =0;i<noOfAccounterMenuItemsInserted ;i++){
        [_accounterMenu removeItemAtIndex:1];
    }
    noOfAccounterMenuItemsInserted = 0;
    noOfMainMenusInserted = 0;
    [menuLinksDictonary removeAllObjects];
    
    
}



#pragma mark -
#pragma mark xml file delegates



- (void)parserDidStartDocument:(NSXMLParser *)parser {
    
}

- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qualifiedName attributes:(NSDictionary *)attributeDict {
    
    if([elementName isEqualToString:@"MenuItem"])
    {
        NSString *name = [attributeDict objectForKey:@"text"];
        
        NSString * shortcut;
        if([attributeDict objectForKey:@"shortcut"])
            shortcut = [attributeDict objectForKey:@"shortcut"];
        else
            shortcut = @"";
        
        menuItem = [[NSMenuItem alloc]initWithTitle:name action:@selector(sendUrl:event:) keyEquivalent:shortcut];
        [menuItem setTarget:self];
        
        if([menuStack count]<1){
            [_accounterMenu insertItem:menuItem atIndex:noOfAccounterMenuItemsInserted+1]; 
            noOfAccounterMenuItemsInserted++;
        }else{
            [(NSMenu*)[menuStack lastObject] addItem:menuItem]; 
        }
        
    }
    else if([elementName isEqualToString:@"Menu"])
    {
        NSString *name = [attributeDict objectForKey:@"text"];
        
        NSMenu*  menu = [[NSMenu alloc]initWithTitle:name];
        [menu setAutoenablesItems:TRUE];
        
        
        NSMenuItem *tempItem = [[NSMenuItem alloc]initWithTitle:name action:nil keyEquivalent:@""];
        [tempItem setTarget:self];
        [tempItem setSubmenu:menu];
        
        if ([menuStack count]>=1) {
            [(NSMenu*)[menuStack lastObject] addItem:tempItem];
        }else{
            
            [_menuBar insertItem:tempItem atIndex:noOfMainMenusInserted+1]; 
            noOfMainMenusInserted++;
        }
        
        [menuStack addObject:menu]; 
    }
    
    else if([elementName isEqualToString:@"Seperator"])
    {
        [(NSMenu*)[menuStack lastObject] addItem:[NSMenuItem separatorItem]]; 
    }
    
    
}


- (void)parser:(NSXMLParser *)parser
 didEndElement:(NSString *)elementName
  namespaceURI:(NSString *)namespaceURI
 qualifiedName:(NSString *)qName
{
    
    if([elementName isEqualToString:@"MenuItem"])
    {
        menuItem = nil;
    }
    else if([elementName isEqualToString:@"Menu"])
    {
        [menuStack removeLastObject];
    }
    else if([elementName isEqualToString:@"Seperator"])
    {
    }
    
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string {
    
    
    
    [menuLinksDictonary setValue:string forKey:[menuItem title]];
    
}

- (void)parserDidEndDocument:(NSXMLParser *)parser {
    
}

#pragma mark -
#pragma mark Sending Menu actions

-(IBAction)sendUrl:(id)sender event:(NSMenuItem *)event
{
    
    NSString *newLink = [menuLinksDictonary valueForKey:[event title]];
    NSLog(@"newLink: %@",newLink);
    [_mainClass changeWebPageLink:newLink];
    
    
}

/*
 -(void)createNewMacMenu{
 
 if(fistTime == TRUE){
 fistTime = FALSE;
 [self create];
 }else{
 
 loadMenuTimer = [NSTimer scheduledTimerWithTimeInterval:1 
 target:self 
 selector:@selector(updateTimer:) 
 userInfo:nil 
 repeats:YES];	
 }
 
 
 }
 
 - (IBAction)updateTimer:(id)sender {
 if(countingTimer == 2){
 
 [self clear];
 [self create];
 countingTimer = 0;
 [loadMenuTimer invalidate];
 loadMenuTimer = nil;
 
 }
 else{
 countingTimer++;
 }
 }
 
 -(NSMenu*)getMenu{
 return _accounterMenu;
 }
 */

@end
