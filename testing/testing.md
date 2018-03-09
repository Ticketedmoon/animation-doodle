## Testing Documents
---
**Table of Contents**
                                      
                                                                                                   
**1. Use Case Testing**
                                    
**2. Usability Testing**

**3. Unit Testing**

**4. Instrumented Testing**

**5. Heuristic Testing**





## 1. Use Case Testing


![](media/use_case.png)




## 2. Usability Testing


**1st Phase of user testing**

During our first phase of user testing we found out a number of key issues which our users felt needed changing.

One of them was how our "Set Background" was implemented. This feature initially just set the background of the frame the user was on when they selected this button. From our users responses we found that they felt that the background should be set for the entire animation, rathar than the user having to set the background for each frame automatically. This showed us how important user testing was going to be for our app, as we were still thinking of using features as software engineers rathar than how the user would interpret things.

Another important point we learned from this phase of testing was where to position the play button. We initially had it hidden inside our menu bar on the top of the screen. We thought that this was a good idea as it freed up space on the page, but users found it a bit difficult to find the button. We have since moved the play button so it's visible on the screen.


**Final Phase of user testing**

**Questionnaire**



![](media/questionnaire.png)


**Questionnaire follow up questions**



![](media/questionnaire2.png)


**Questionnaire Responses**


**Question 1**


![](media/bar_chart1.png)


 - The majority of users found the app easy to use and were able to intuitively use the features and buttons.


**Question 2**


![](media/bar_chart2.png)


 - As you can see the majority of users were able to intuitively work out what each feature did. One user though struggled to find where the play button was located. We have subsequently moved it to a more prominent position on the screen.


**Question 3**



![](media/bar_chart3.png)


 - All users were happy with the automatic nature of the onion skinning process.




**Question 4**


![](media/bar_chart4.png)


 - Most users found it comfortable with the design of the app and  where able to move fluidly between screens


**Question 5**


![](media/bar_chart5.png)


 - Almost all users would use the app again.



**Question 6**


![](media/bar_chart6.png)


 - Almost all users would recommend this app to a friend.


**Questionnaire follow up responses**



**Would you prefer an eraser feature to remove errors or did you like the undo button we had ?**


 - No, I thought the undo button worked well

 - No the undo button was ok

 - Yes, I'm used to erasing objects rather than pushing a button

 - Yes, I enjoy the aspect of rubbing stuff out on the screen

 - No, I thought the undo button was easier to use than an eraser

 - I didn’t see any difference between the two

- Include both options






**How did this animation app compare to any other similar apps you may have used ?**


 - Better laid out than other drawing apps i’ve used.

 - Less clunky than others. Onion skinning feature was very nice and helpful.

 - Other apps allow you to drag and drop objects in the drawing which your app doesn’t have, other than that I thought it was a lot easier and quicker to use than some others. Onion skinning very handy to have

 - I was very quickly able to start creating animations which I thought was better than other similar apps

 - I haven’t used any other drawing apps, but liked the whole layout of this app. Liked that it wasn't just about drawing, you could see other people's animations as well

 - I enjoyed the section where I could see and rate other people's animations. That was quite different to other drawing apps. It created a bit of a "social media feel" to the app.


**Which features were you impressed with the most ?**


 - Onion Skinning

 - Public Animations page.

 - Rating the videos

 - Onion Skinning

 - Liked being able to get my animation rated by others

 - Onion Skinning


**Are there any features you would have liked to have been included ?**


 - Drag and drop objects in the drawing

 - Being able to add in objects like people or houses into the drawings

 - Being able to move objects with my fingers

 - Can’t think of any

 - A feature that makes the paint brush turn charcoal


## 3. Unit Testing

*Uri bug*

During our unit testing we found an important bug related when our video url links were being stored in the database. Certain videos weren't playing properly due to Uri parsing issues.

![](media/unitTest.png)


*Video rating bug*

We also found a bug related to how the videos ratings were changing. When no rating took place our rating counter was stil being incremented. This was reducing the average of the video even though no rating had been given. This was dramatically reducing the rating of the video, and we couldnt understand why unti we had implemente the unit test for this feature. We then found and fixed the problem easily

 
![](media/unitTest2.png)
 

## 5. Heuristic Testing


We are using Schneiderman's 8 golden rules to help guide us through the design of our application.


**Strive for consistency**


We have ensured that the design and layout of our app remains consistent throughout. We have used consistent colouring throughout all screens in the app.



**Offer informative feedback**


We offer informative feedback to users when they input incorrect data in the login fields.  As you can see in the pictures below, when a user inputs an incorrect email or password input. This helps the user to see where they are going wrong and to correct it. 

                                                
![](media/RegisterError.png)



**Design dialogs to yield closure** 


When a user completes a task the app relays the result of the command to the user. This enables the user with the confidence and knowledge that their task was completed. They are no longer wondering if their actions were received or not. An example of this can be seen when a user logs in , uploads an animation.


![](media/closure.png)





      
   

