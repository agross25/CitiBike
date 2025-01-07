# CitiBike Map Application
A Java-based map viewer application using OpenStreetMap and Swing, capable of displaying routes, waypoints, and interactive map features.

# Application Overview
Map Application is split into 3 parts: Component, Frame, Controller
1. MapComponent 
   - extends JXMapViewer and constitutes the actual map visual that appears to the user
   - uses WaypointPainter to paint waypoints where user clicks on the map
   - uses custom class RoutePainter (@author Martin Steiger) to paint routes between closest stations to selected waypoints
   - implements mouse listeners to respond to user's movement across the map and clicks
2. MapFrame
    - extends JFrame and constitutes the entire frame that appears to the user, which holds the MapComponent
    - uses a BorderLayout to center map with a GridBagLayout to hold Labels and Buttons below the map
    - Start and End labels to display coordinates of the selected Start and End points the user wishes to travel between
    - Clear Button to clear the user's selections
    - Map Button to map out the fastest possible route between selected points
3. MapController
    - as the controller, it has a frame to display and controls the functionality of the frame's elements
    - sets an ActionListener for the Map Button:
      - calls LambdaServiceFactory (explained below) to call our CitiBikeService lambda which calculates fastest route between selected points
      - calls displayRoute() method to paint the route via the component
    - implements an addPoint() method which adds a waypoint wherever the user clicks (up to 2 maximum)
    - sets an ActionListener for the Clear Button that clears the waypoints, painted route, and re-centers the map

# Map Screenshots

![User's selected points before mapping](/Users/adinagross/IdeaProjects/CitiBike/src/main/resources/screenshots/WaypointsScreenshot.png)
![Mapped Route](/Users/adinagross/IdeaProjects/CitiBike/src/main/resources/screenshots/MappedScreenshot.png)

# Lambda Overview
AWS Lambda is essentially a service that allows us to request execution of certain sets of code without managing servers.
In this project, AWS Lambda is used to calculate the best and fastest route between two points by finding the closest CitiBike stations to the given start and end coordinates.

## Lambda Components
1. CitiBikeService, CitiBikeServiceFactory, StationResponse, StatusResponse, and CitiBikeFunctions
These components work together to perform 3 operations related to CitiBike stations:
   - retrieving the status of a station given its ID
   - finding the closest station to a given point with available bikes or
   - finding the closest station to a given point with available slots

Station Response - holds a list of StationInfo objects, modeled to represent a CitiBike station, includes relevant attributes such as name, ID, latitude-longitude

Status Response - holds a list of Status objects, modeled to represent status of a CitiBike station, including its ID and the number of bikes and slots available

CitiBikeService: Defines the API for interacting with CitiBike-related data. Tells the machine where to access relevant JSON files which will provide the desired info

CitiBikeServiceFactory: Responsible for creating instances of CitiBikeService via Retrofit

CitiBikeFunctions: Implements the logic for finding the closest station to a given point and determining station statuses. It is called by the Request Handler to retrieve the required data.

2. CitiBikeRequestHandler, LambdaService, LambdaServiceFactory, Request, and Response
These components facilitate the integration with AWS Lambda, allowing the application to calculate routes by finding the closest available CitiBike stations to the start and end points.

Request - represents the input data model for the Lambda function, including from (start coordinates) and to (end coordinates)

Response - represents the output data model from the Lambda function, including original from and to locations, start (closest starting station), and end (closest ending station)

CitiBikeRequestHandler: Implements the Lambda RequestHandler interface to processes incoming requests (in the form of JSON data) and uses CitiBikeFunctions to find the closest stations with available bikes and docks. The handler returns the calculated route information as a response.

LambdaService: the service that interacts with AWS Lambda by invoking the Lambda function and handling request-response communication

LambdaServiceFactory: responsible for creating and configuring instances of LambdaService to actually send requests and get responses