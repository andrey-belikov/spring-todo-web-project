###1. Build image:

docker build -t todo-web-project .

###2. Run in development mode with internal in memory DB:
   
docker run -it --rm  --name todo-web-project -p 8081:8081 todo-web-project

or in detached mode 

docker run -d --name todo-web-project -p 8081:8081 todo-web-project

it will be accessible by **http://dockerHost:8081/todo**

###2.1 Run in interactive mode, and remove container after stop:

docker run --rm -it --name todo-web-project -p 8081:8081 todo-web-project

