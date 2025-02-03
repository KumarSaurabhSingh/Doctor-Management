The &quot;HealthConnect&quot; API serves as a robust Doctor Management system, enabling users to effortlessly establish, oversee, and locate physicians according to diverse parameters. This project aims to assess your proficiency in programming and logical thinking. Each task is accompanied by a pre-defined description and illustrative examples to support your progress.

I have divided the whole project into 11 task, which will be helpful for you to understand.

Also you find <strong>test.py</strong> file in which full test code is also designed for testing each endpoint with all the edge cases.

Command to run: mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$PORT

<h3>Task Description: </h3>

<h4>Task 1: Seamless Doctor Profile Creation API Implementation</h4>
<p><strong>Perform the following tasks before API development:</strong></p>
<ul>
<li>Download the MySQL database by clicking on the provided link. <a href="https://drive.google.com/file/d/1Gs9Ej7B-LCW6KKL15Q-C4J_blC6J0M0R/view?usp=sharing" target="_blank" rel="noopener"><strong>HealthConnect.sql</strong></a></li>
<li>Configure your database connection in the <em>"<strong>src/main/resources/application.properties</strong>"</em> file, specifying the database name, username, and password.</li>
</ul>
<p>This task revolves around developing a streamlined system for creating and exploring doctors' profiles within a medical application. The core objective is to implement a <strong>POST </strong>endpoint, "<strong>/doctors/create</strong>" which empowers users to establish new doctor profiles. This endpoint is designed to guarantee the uniqueness of both doctor email addresses and IDs, while also facilitating the customization of the doctor's weekly schedule encompassing workdays and available time slots.</p>

<p><strong>Input:</strong></p>
<p>The input JSON should include the following details:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; "email": "jane.smith@example.com",</em><br><em>&nbsp; &nbsp; "specialization": "Pediatrician",</em><br><em>&nbsp; &nbsp; "weeklySchedule": [</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Monday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Tuesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "09:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "16:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Wednesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Thursday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "09:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "16:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Friday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; }</em><br><em>&nbsp; &nbsp; ]</em><br><em>}</em></p>
<p><strong>Output:</strong></p>
<p>The success response will include &nbsp;as follows:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp;&nbsp;</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "id": 1,</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "email": "jane.smith@example.com",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "specialization": "Pediatrician",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "weeklySchedule": [</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Monday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Tuesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "09:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "16:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Wednesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Thursday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "09:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "16:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Friday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; ]</em><br><em>&nbsp;&nbsp;</em><br><em>&nbsp; &nbsp;</em><br><em>}</em></p>

<h4>Task 2: Comprehensive Doctor Profile Management API Enhancement</h4>
<p>Building upon the previous task, this enhancement involves implementing a GET endpoint to retrieve a comprehensive list of all registered doctors within the application. The <strong>GET </strong>endpoint, "<strong>/doctors,</strong>" is introduced to facilitate this functionality. The response to this endpoint will be presented in JSON format, containing vital details of all doctors registered in the "<em>doctors</em>" table.</p>

<p><strong>Output:</strong></p>
<p>The output response is formatted in JSON and includes essential doctor details, such as their ID, name, email, and specialization. An example response would be:</p>
<p style="padding-left: 40px;"><em>[</em><br><em>&nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "id": 1,</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "email": "jane.smith@example.com",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "specialization": "Pediatrician"</em><br><em>&nbsp; &nbsp; }</em><br><em>]</em></p>
<p>If no doctors details are found in the table then response would be:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp;"success": false</em><br><em>&nbsp; &nbsp;"message":"Doctor not found for hospital."</em><br><em>}</em></p>

<h4>Task 3: Efficient Doctor Specialization Search API Implementation</h4>
<p>This task introduces a powerful feature to the doctor profile management system - the ability to search for doctors based on their specialization. It ensures implementing a&nbsp;<strong>GET </strong>endpoint, "<strong>/doctors/search</strong>," which empowers users to find doctors specializing in a specific area. If no specialization matches the criteria or if no doctors are found, a suitable response is generated.</p>

<p><strong>Input:</strong><br>URL: /doctors/search?specialization={specialization}</p>
<p><strong>Output:</strong></p>
<p>An example of the response is:</p>
<p style="padding-left: 40px;"><br><em>[</em><br><em>&nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "id": 1,</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "email": "jane.smith@example.com",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "specialization": "Pediatrician"</em><br><em>&nbsp; &nbsp; }</em><br><em>]</em></p>
<p>If no doctor related to given {specialization} are found then response would be:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp;"success": false</em><br><em>&nbsp; &nbsp;"message":"No Doctors found &nbsp;matching the specialization {specialization} "</em><br><em>}</em></p>

<h4>Task 4: Comprehensive Doctor Information Retrieval API Implementation</h4>
<p>This task enhances the doctor profile management system with a <strong>GET </strong>endpoint, "<strong>/doctors/{id}</strong>," that provides detailed information about a specific doctor based on their unique ID, including their weekly schedule. The endpoint ensures the validity of the provided {id} against the data stored in the "<em>doctors</em>" table.</p>

<p><strong>Output:</strong></p>
<p>The response will include the doctor details as follows:</p>
<p style="padding-left: 40px;"><em>{</em><br><em>&nbsp; &nbsp;&nbsp;</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "id": 1,</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "email": "jane.smith@example.com",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "specialization": "Pediatrician",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "weeklySchedule": [</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Monday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Tuesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "09:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "16:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Wednesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Thursday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "09:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "16:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Friday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; }</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; ]</em><br><em>&nbsp;&nbsp;</em><br><em>&nbsp; &nbsp;</em><br><em>}</em></p>
<p>If doctor with {id} not found in the "<em>doctors</em>" table , then response would be:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp;"success": false</em><br><em>&nbsp; &nbsp;"message": "Doctor not found with ID: {id} "</em><br><em>}</em></p>
<p>&nbsp;</p>

<h4>Task 5: Empowering Doctor Details Update API Functionality</h4>
<p>This task introduces a pivotal functionality to the doctor profile management system through the implementation of a <strong>POST </strong>endpoint, "<strong>/doctors/update/{id}</strong>," enabling users to modify the details of a specific doctor based on their unique ID. The endpoint ensures email uniqueness validation and permits updates to the doctor's name, email, specialization, and weekly schedule.</p>

<p><strong>Input:</strong></p>
<p>The input JSON should include the following details:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; "email": "jane.smit1h@example.com",</em><br><em>&nbsp; &nbsp; "specialization": "Pediatrician",</em><br><em>&nbsp; &nbsp; "weeklySchedule": [</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Monday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Wednesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Friday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; }</em><br><em>&nbsp; &nbsp; ]</em><br><em>}</em></p>
<p><strong>Output:</strong></p>
<p>Upon successfull validation , the success Response would be:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp; "name": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; "email": "jane.smit1h@example.com",</em><br><em>&nbsp; &nbsp; "specialization": "Pediatrician",</em><br><em>&nbsp; &nbsp; "weeklySchedule": [</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Monday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Wednesday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; },</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "dayOfWeek": "Friday",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "startTime": "10:00",</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "endTime": "17:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; }</em><br><em>&nbsp; &nbsp; ]</em><br><em>}</em></p>

<h4>Task 6: Seamless Doctor Deletion and Appointment Management API Integration</h4>
<p>This task introduces a critical functionality, implementing a <strong>POST</strong> endpoint, <strong>"/doctors/delete/{id},"</strong> enabling to delete a specific doctor based on their unique ID.</p>

<p><strong>Output:</strong></p>
<p>The success response include:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp; "success": true</em><br><em>}</em></p>

<h4>Task 7: Efficient Doctor Leave Management</h4>
<div>This task,&nbsp; implements a <strong>POST</strong> endpoint <strong>"/doctors/{id}/leave"</strong> for efficient Doctor Leave Management in our Doctor Management API. These endpoints will allow doctors to add and update their leave schedules, providing an intuitive way for doctors to manage their availability effectively.</div>
<div>&nbsp;</div>
<div>

<div><strong>Input:</strong></div>
<div>&nbsp;</div>
<div style="padding-left: 40px;"><em>{</em></div>
<div style="padding-left: 40px;"><em>"leaveDate": "2023-08-15",</em></div>
<div style="padding-left: 40px;"><em>"startTime": "09:00",</em></div>
<div style="padding-left: 40px;"><em>"endTime": "12:00"</em></div>
<div style="padding-left: 40px;"><em>}</em></div>
<div>&nbsp;</div>
<div><strong>Output:</strong></div>
<div>Upon successful validation, the system either updates an existing leave entry or creates a new one. It responds with a JSON message indicating the success of the process.</div>
<div>&nbsp;</div>
<div style="padding-left: 40px;"><em>{</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; &nbsp;"DoctorId": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; &nbsp;"LeaveId": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; &nbsp;"LeaveDate": "2023-08-15",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; &nbsp;"StartTime": "09:00",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; &nbsp;"EndTime": "12:00"</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp;&nbsp;</em></div>
<div style="padding-left: 40px;"><em>}</em></div>
<div>&nbsp;</div>
<div>Note: For output if start time or endtime is not there , then display "Not specified" instead of null.</div>
<div>Example:</div>
<div>&nbsp;</div>
<div style="padding-left: 40px;"><em>{</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "Doctor Id": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "LeaveId": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "LeaveDate": "2023-08-15",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "StartTime": "09:00",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "EndTime": "Not specified"</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp;&nbsp;</em></div>
<div style="padding-left: 40px;"><em>}</em></div>
<div>&nbsp;</div>

<h4>Task 8: Efficient Leave Removal for Doctors</h4>
<div>
<p>This task provides the POST endpoint "/doctors/{id}/deleteLeave" empowers doctors to remove their leave entries based on the provided leave date. This functionality enriches the Doctor Leave Management system, allowing doctors to efficiently update their availability as needed.</p>

<p><strong>Endpoint: POST /doctors/{id}/deleteLeave?leaveDate={leaveDate}</strong></p>
<p><strong>Input:</strong><br>Doctor ID (id): The unique ID of the doctor whose leave entry needs to be deleted. It is provided in the path variable.<br>Leave Date (leaveDate): The specific date for which the doctor wants to remove the leave entry. It is provided in the path variable using the ISO date format (e.g., 2023-08-10).</p>
<p>Example Input:/doctors/1/deleteLeave?leaveDate=2023-08-10</p>
<p><strong>Output:</strong></p>
<p>Upon successfull validation , the leave entry is found and &nbsp;is deleted from the "<em>doctor_leave</em>" table,the success response include:</p>
<p style="padding-left: 40px;"><br>{<br>&nbsp; &nbsp; "success": true<br>}</p>

<h4>Task 9: HealthConnect API: Check Doctor Availability</h4>
<div>
<p>This task focuses on the implementation of a <strong>GET </strong>endpoint "/<strong>doctors/{id}/availability?dateTime={dateTime}</strong>" that enables users to check a doctor's availability for a specific date and time. By incorporating this feature, the HealthConnect API aims to provide users with the ability to conveniently determine whether a doctor is accessible for appointments.The availability will be based on the doctor's weekly schedule. Additionally, the endpoint will provide information about the doctor's leave status if the date falls within a leave period.&nbsp;</p>

<p><strong>Input:</strong></p>
<p>ENDPOINT: /doctors/{id}/availability?dateTime={dateTime}</p>
<p>Doctor ID (id): The unique ID of the doctor for whom availability is being checked. This is provided in the path variable.<br>Appointment Date and Time (dateTime): The desired appointment date and time for which the doctor's availability is being checked. This is provided in the query parameter and follows the format "yyyy-MM-ddTHH:mm" (e.g., "2023-07-27T15:30").</p>
<p><strong>Output:</strong></p>
<p>Success Response:<br>If the doctor is available and an available slot is found, the response will look like this:</p>
<p style="padding-left: 40px;"><em>{</em><br><em>&nbsp; &nbsp; "success":true,</em><br><em>&nbsp; &nbsp; "message": "yes doctor is available at given date and time: {dateTime}"</em><br><em>}</em></p>
<p>If doctor is not available , then in response include in the message why doctor is not avaialble.</p>

<h4>Task 10: Efficient Appointment Scheduling</h4>
<div>
<div>In this task, we will implement a powerful endpoint to provide advanced appointment booking functionality with a comprehensive patient management system, specifically designed for individual doctors. This endpoint ensures availability and validation checks, enhancing the Doctor Management API by facilitating seamless appointment scheduling.</div>
<div>&nbsp;</div>
<div>This&nbsp; implement a <strong>POST </strong>endpoint "<strong>/doctors/{id}/bookAppointments</strong>" to allow users to create an appointment for a specific doctor. The endpoint takes the doctor's unique ID as a path parameter and the desired appointment date and time, patient's name and contact information as input in the request body. The API performs various validations and checks to ensure a smooth appointment booking process.</div>
<div>&nbsp;</div>
<div>

<div><strong>Input:</strong></div>
<div>&nbsp;</div>
<div style="padding-left: 40px;"><em>{</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "name": "John Singh",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "contact": "7260920237",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "dateTime": "2023-07-31T15:00:00"</em></div>
<div style="padding-left: 40px;"><em>}</em></div>
<div style="padding-left: 40px;">&nbsp;</div>
<div>&nbsp;</div>
<div>id: The unique ID of the doctor for whom the appointment is to be created.</div>
<div>dateTime: The desired appointment date and time in the format "yyyy-MM-ddTHH:mm:ss" (e.g., "2023-07-27T15:30:00").</div>
<div>&nbsp;</div>
<div><strong>Output:</strong></div>
<div>&nbsp;</div>
<div>Upon sucessfully making appointment&nbsp; for the patient , the success response is:</div>
<div>&nbsp;</div>
<div style="padding-left: 40px;"><em>{</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "AppointmentId": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "DoctorId": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "DoctorName": "Dr. Jane Smith",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "DoctorEmail": "jane.smith@example5.com",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "DoctorSpecialization": "Pediatrician"</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "PatientId": 1,</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "PatientName": "John Singh",</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "PatientContact": "7260920237"</em></div>
<div style="padding-left: 40px;"><em>&nbsp; &nbsp; "dateTime": "2023-07-31T15:00:00"</em></div>
<div style="padding-left: 40px;"><em>}</em></div>
<div>&nbsp;</div>

<h4>Task 11: Get Upcoming Appointments for Doctor Endpoint: Efficient Appointment Retrieval</h4>
<div>
<p>This implements a <strong>GET</strong> endpoint "<strong>/doctors/{id}/appointments/upcoming</strong>" to retrieve all upcoming appointments for a specific doctor. The endpoint takes the doctor's unique ID as a path parameter. It queries the database for all appointments of the specified doctor that are scheduled after the current date and time. The retrieved appointments are then grouped by date, and the response includes the doctor's ID, name, specialization, and a map of upcoming appointment dates with corresponding time slots.</p>

<p><strong>Input:</strong></p>
<p>Endpoint: GET /doctors/{id}/appointments/upcoming</p>
<p>{id}: The unique ID of the doctor for whom upcoming appointments are to be retrieved.</p>
<p><strong>Output:</strong></p>
<p>The success response include:</p>
<p style="padding-left: 40px;"><br><em>{</em><br><em>&nbsp; &nbsp; "doctorId": 1,</em><br><em>&nbsp; &nbsp; "doctorName": "Dr. Jane Smith",</em><br><em>&nbsp; &nbsp; "specialization": "Pediatrician",</em><br><em>&nbsp; &nbsp; "upcomingAppointments": {</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; "2023-07-27": [</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "15:30:00"</em><br><em>&nbsp; &nbsp; &nbsp; &nbsp; ]</em><br><em>&nbsp; &nbsp; }</em><br><em>}</em></p>
