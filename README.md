#### CA326 3rd Year Project
---

![](/user_manual/media/title_small.png)

##### What is Animation Doodle?
Animation Doodle is a project being developed by James Collins & Shane Creedon in an effort  
to unite both drawing and artistic design with animation inside a mobile application.  

---
##### How to open individual project sections (E.G.):
- Sign-In Screen
- Profile Screen
- Start Drawing Screen

> To work on / open individual parts of the project and push them back using version control (Git)  
> simply clone / pull the project (git clone <Project_URL> / git pull origin master.  
  
> Then navigate to the code section where you will find a list of directory's representing each of  
> the possible activities / screens that are present in the App.  
  
> Simply choose whichever screen you would like to work on and open that particular folder (screen) in android studio.  

---
1. Branching:
	-  Be sure to branch so we don't overwrite each others work.  
	-  **git checkout <branchName>** will swap branches.  
	-  **git Branch** will show you all working branches.  
2. Add / Commit / Push:
	- **git Commit & Push** changes to your branch.
3. Merging:
	- **git merge <branchName>** will merge your current branch with the  
	  branch you pass, I.E. git merge shanes_branch.  
	  Given that I am on master, master will merge with shanes_branch.  
	- When you are checking out to master to merge your branch,  
	  **Remember to git pull** before you merge.  
	  Git will probably issue a warning before anyway.  
4. Git Status:
	- **git status** will tell you which files have / have not been added or commited.  

---

###### Tools / Resources:
1. The primary tool we will be using will be `Android Studio 3.0.1` to build our  
   mobile application.  
2. We will be using `GIT` for version control (Gitlab)  
3. We will be using Google's `Blockly API` to allow basic animation creation with code blocks.  
4. We will be using an `PHP & SQL` to enable database connections and querying.  
5. We will be using several API's and librarys to allow drawing / animation functionality  
   within our application. These will be specified at a later date.  

---
