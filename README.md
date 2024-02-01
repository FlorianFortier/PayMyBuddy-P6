`# PayMyBuddy-P6

This application strives for more secure payments and better budget control.

## Things to know before starting the app

Default port has been change to  : **23904**

So here's the local url  : http://localhost:23904

Database is located to : **jdbc:mariadb://localhost:3306/pay_my_buddy**

### Please read carefully these instructions :

**1.    I recommand using my data set for testing the application to do so please run the script located at ***"/src/main/resources/testData/initDb.sql"*** then check values in user table for login. Feel free to test registering**

**2.   There is a mailer that points to my mailtrap. It is used by registration for sending comfirmations links.
To bybass controls on the confirmed account please set to <span style="color: #26B260">***TRUE***</span> the enabled field in user table after registering**

**3. At the moment you cannot link within the App the bank and your payMyBuddy account to do so please update your user in DB bank_id with the desired bank and in the bank table 'user_id' with your user**


## Documentation
### UML Diagram
![image](https://github.com/FlorianFortier/PayMyBuddy-P6/assets/78166457/02c75f28-841a-4ef3-87ca-f66acbbc7e5a)



### Physical Data Model
![exported_from_idea drawio](https://github.com/FlorianFortier/PayMyBuddy-P6/assets/78166457/7124fcb2-10b0-404d-b2a2-ef7b46bb34ab)
