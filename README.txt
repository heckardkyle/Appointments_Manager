Developed in my C195 Software II - Advanced Java Concepts course at WGU. This Course Demonstrates my ability to program both develop a GUI in Java and communicate with a database.

Appointment Manager

Created by:
Kyle Heckard
khecka2@wgu.edu
4/14/2024

IntelliJ IDEA 2023.2.2 (Community Edition)
Build #IC-232.9921.47, built on September 12, 2023
Runtime version: 17.0.8+7-b1000.22 amd64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
Windows 10.0
GC: G1 Young Generation, G1 Old Generation
Memory: 2048M
Cores: 4

Kotlin: 232-1.9.0-IJ9921.47

Java SE 17.0.1
JavaFX-SDK-17.0.1
mysql-connector-java-8.0.25



The purpose of this application is to Create and Store Appointments, Customer information, and Generate Reports.

After signing in to the application with a username and password, the user will be presented with three tabs:
    Appointments
    Customers
    Reports

Appointments Tab:
This tab is for viewing created appointments. By default, the table will be showing the current months appointments.
The user can change the table to show weekly appointments by clicking the weekly button. By selecting a date in the
datePicker, the table will show appointments for the month or week of the selected date, depending on if the monthly
or weekly button is selected.
At the bottom of the screen, the buttons "Add New Appointment", "Update Selected Appointment", "Delete Selected
Appointment", and "Logout" are visible. By default, the "Update Selected Appointment" and "Delete Selected Appointment"
are disabled by default until an appointment is selected.

"Add New Appointment" allows the user to create a new appointment. An appointment ID will automatically be generated for
the new appointment and cannot be changed.

"Update Selected Appointment" allows the user to update the Appointment selected in the table. All fields will be filled
with the appointment's current values.

"Delete Selected Appointment" allows the user to permanently remove an appointment.

"Logout" will log the user out of the application and return the user to the login screen.


Customers Tab:
This tab is for viewing all existing customers.
At the bottom of the screen, the buttons "Add New Customer", "Update Selected Customer", "Delete Selected Customer", and
"Logout" are visible. By default, the "Update Selected Customer" and "Delete Selected Customer" are disabled by default
until a customer is selected.

"Add New Customer" allows the user to create a new customer. A customer ID will automatically be generated for the new
customer and cannot be changed.

"Update Selected Customer" allows the user to update the Customer selected in the table. All fields will be filled with
the customer's current values.

"Delete Selected Appointment" allows the user to permanently remove a customer. Upon deletion, all existing appointments
associated with the deleted customer will also be permanently removed.

"Logout" will log the user out of the application and return the user to the login screen.


Reports Tab:
The Reports Tab is used for generating various report types.
There are three accordion containers labeled "Appointments", "Contacts", and "Users" with report options for each type.
After selecting a report type. Select the options available in the ComboBoxes and report will be generated in the table
based on the options selected.
A "Logout" button is available to log the user out of the application and return the user to the login screen.


For part A3f of the PA, I chose to add an Appointments by User report the shows all the appointments created by a
selected User. This report can be useful for performing audits or monitoring appointment entries by users.
