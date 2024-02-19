# PayMyBuddy-P6

This application strives for more secure payments and better budget control.

## Things to know before starting the app

Default port has been change to  : **23904**

So here's the local url  : http://localhost:23904

Database is located to : **jdbc:mariadb://localhost:3306/pay_my_buddy**

### Please read carefully these instructions :

**1. Firstly you need to run the scripts ***create_db.sql*** in your own MariaDB environnement, Also i recommand using my data set for testing the application to do so please run the script located at ***"/src/main/resources/testData/add_test_data.sql"*** then check values in user table for login. Feel free to test registering**

**2.   There is a mailer that points to my mailtrap. It is used by registration for sending comfirmations links.
To bybass controls on the confirmed account please set to <span style="color: #26B260">***TRUE***</span> the enabled field in user table after registering**


## Documentation
### UML Diagram
![diagram-4246493688623003328](https://github.com/FlorianFortier/PayMyBuddy-P6/assets/78166457/47c69ca2-94be-4f4b-a327-f5e855817caf)

### Physical Data Model
![pmb_diagram_mpd](https://github.com/FlorianFortier/PayMyBuddy-P6/assets/78166457/6bd4a9a0-03aa-4e1e-b2e0-146ebde347c4)
