Desktop Scheduling Application
C195 Course Version 4
Daniel Tierney
dtiern5@wgu.edu
July 19, 2021

Built with:
IntelliJ IDEA 2020.3.2 (Community Edition)
Java SE 11.0.10
JavaFX-SDK-11.0.2
mysql-connector-java:8.0.22



Purpose:
    This application is designed for a fictional consulting organization. It allows for simple interaction with a database,
    including adding to, deleting, and modifying database records for Customers and Appointments. The organization operates
    in multiple time zones, and with multiple languages. This application ensures an ease of use for end users by displaying
    all appointment times in the user's local time, and ensures integrity of data by storing those same appointment times
    in UTC. It also disallows scheduling outside of business hours (8am - 10pm EST) and weekends in order to reduce
    scheduling errors.


Directions:
    Log in with username: test and password: test
    Log in screen will display in English or French based on the user's system default


    Main screen:
    Appointment table can display appointments by week or month by using the radio buttons. The week or month displayed is
    based on the selected value in the datePicker object.

    The add, modify, and delete buttons for appointments are to the right of the table. An appointment must be selected in
    order to delete or modify. The add and modify buttons will take the user to the respective screen.

    The functionality for adding and removing customers is similar, although a customer can not be deleted if it has
    associated appointments (must delete appointments first).

    The three reports are on separate screens accessed by the buttons in the lower right corner.


    Add Appointment screen:
    All fields require values in order to add an appointment. The 'Start Time' ComboBox populates all possible times in
    the user's time zone (automatically converted from 8am to 10pm EST). The 'End Time' ComboBox then populates with all
    valid times between the start time and the final available time for the day. Appointments can not be scheduled outside
    of these hours or on weekends.


    Modify Appointment screen works in identical fashion to the add appointment screen


    Add Customer screen:
    All fields require values in order to add a customer. The 'Division' ComboBox populates based on the Country from
    the 'Country' ComboBox.


    Modify Customer screen:
    All fields require values in order to update a customer. This screen displays a table with all current customers, and
    the 'Select' button will populate the fields with their information in order to modify. A different customer can be
    selected by using the 'Clear Selection' button to enable the table.


    Reports:
    The Appointment Report screen allows the user to select a year and month. The total number of customer appointments by
    type are then displayed.

    The Contact Schedule Report screen allows the user to select a contact. All of that contact's associated appointments
    are then displayed.

    The Customer Location Report is the third report of my choice outlined in A3f of the requirements. The user selects a
    country, and the table displays a breakdown of how many customers are in each first level division of that country.
    Empty divisions are not shown.