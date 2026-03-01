# Milestone 6
CST-339: Programming in Java III  
Justin Albecker  
3/1/2026

---

## Planning Documentation
### Initial Planning
Going into this week, I was ready to implement security into my website. My plan was to make small changes this week to ensure proper password encryption, and other security requirements for this milestone assignment.
### Retrospective Results
This week's milestone update was more difficult to implement than I expected. I felt this week's activity assignment did not prepare me for implementing Spring Security into my project. That being said, I was able to successfully implement what I wish to.
## Design Documentation
### General Technical Approach

### Key Technical Design Decisions

### Risks/Bugs Remaining
A major issue that I realized before submission is that I did not encrypt username's as well. While doing this week's updates, my thought process was that as long as the passwords are encrypted, that should be enough. I now realize that even if passwords are encrypted, any account access information is dangerous for potential hacks/leaks. This will be updated next week.
## Photos
### Website Flowchart
```mermaid
---
title: Flowchart 1
---
flowchart TD
    %% Nodes
        A("User opens website")
        B("Login page automatically opens")
        C{"Login or Register?"}
        D{"User Enters Correct Credentials?"}

        E("Registration page opens")
        F("User enters name, email, password")
        G("Password gets encrypted and stored in database")
        
    %% Edge connections between nodes
        A --> B --> C
        C -- Login --> D 
        C -- Register --> E --> F --> G --> C

```
``` mermaid
---
title: Flowchart 2
---
flowchart TD
    %% Nodes
        D{"User Enters Correct Credentials?"}
        D1("Collections page opens")
        D2("User clicks on add movie to collection button")
        D3("Movie added to collection")
        D4("User clicks on delete movie button")
        D5("Movie deleted from collection")
        D6("User clicks on edit movie button")
        D7("Movie details updated")
        D8("User switches collections")
        D9("Visible collection changes accordingly")
        D10("User clicks on logout button")
        D11("User is logged out and redirected to login page")

        Da("Error message appears")

        END("END")
        
        

    %% Edge connections between nodes
        D --> D1 
        D1 --> D2 --> D3 --> END
        D1 --> D4 --> D5 --> END
        D1 --> D6 --> D7 --> END
        D1 --> D8 --> D9 --> END
        D1 --> D10 --> D11 --> END
        D --> Da --> END
```

### Sitemap
![Sitemap](images/sitemap.png)
### Proof of Encryption
![Proof of Encryption](images/encryption.png)
## Links

- [Video Explanation Link](https://youtu.be/I_fIKBmmlYI)
