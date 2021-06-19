# MedicalService High Level Design

1. Architecture

   1.1 Load Balancer
       
       Load balancer is used to archive load balancing, avoid single point failure.
       
       option1 stateless FE
           Based on the type, request will be routed to one of the FE servers (cluster)
       option2 stateful FE
           Request will be routed to one FE server which is responsible for the users in this hash.
       
   1.2 FE Servers/Cluster & Services
       
       Services, such as UserService, VisitService, can be deployed to 1 or multiple FE Servers to 
       handle service-specific requests.
       
       A FE server may host 1 or multiple Services. 
       FE servers hosting the same service logically forms a service cluster.
       
       option1 Stateless-no-sharding
       
       In the case of no DB sharding implemented, each FE server (or more specifically the microservice 
       running on it) handles requests no matter from which user, read/write data from/to DB layer directly.
       
       option2 Stateless-sharded
       
       In the case of DB sharding implemented, each FE server will be assigned to requests from user in a 
       particular shard, read/write data from/to the particular shard of DB layer directly.
       
       option3 Stateful-sharded
       
       In the case of DB sharding and caching implemented, each FE server will be assigned to requests 
       from user in a particular shard, read/write data from/to the cache directly. 

   1.3 Caching
   
       It is not expected that patient profile (User) or medical data set (Visit) change often, it fits 
       one-write-multiple-read pattern. Therefore caching is ideal for improving data retrieval.
              
       For read request, FE server (or more specifically the microservice running on it) fetch data from 
       cache first. In case of miss, it then from DB layer and the data resides in cache.
       
       For write request:
       option1: write-through 
       
       Since the system is expected far less write than read and it is affordable for the time needed for
       writing to both the cache and the DB layer, we would recommended this as mainly for avoid any 
       potential data inconsistency raised during host recovery/diaster recovery.
       
       optoin2: write-back(aka write-deferred).
       This is good for performance and if eventual-data-consistency is acceptable (in the system it is).
          
   1.4 Data Servers

       optional SQL RDBMS
       
       Given most information the system process are structured data set, human generated (instead of
       automatically generated), a SQL RDBMS is chosen for data persistence layer.
       
       In the case of high volumn of data need to be stored/processed, we can do sharding for volume/capacity
       as well as retrieval performance.
       
       option2 No SQL
       
       No SQL is mainly for schema-free, scalability, high availabilitythe, and eventual consistency. 
       In this case theses are not what the system is for, hence not a good candidate.
       
       
2. Schema
    
    See package model.
    
3. Auditing
    
    Auditing is enabled to capture the change history. Any change (create, update) in User/Visit 
    automatically add an entry in AuditStore.
     
    The AuditStore is generic and can be used for other data set auditing.
 
4.  Anonymized data 
    
    Anonymized data can be collected by extracting each participating patient's non-personal-identifying 
    data (from both User Store and Visit Store) which is newer than the last extraction run, per study.
    
    This extraction can be run on a pre-defined interval like a cron job.
    
5. Deployment

    Use Terraform define the deployment manifests.
    Use Spinnaker pipeline to deploy various resources, such as Load Balancer, Target Group, IAM Role and
    Policy, Host from an ami-image, ASG to maintain the min/max nodes of the service.
    The actual hosts built can be AWS EC2, ECS or EKS, or its Azure/GCP equivalents.

6. Permission

   There are four operations on any data set, namely, CREATE, RETRIEVE, UPDATE and DELETE. And there are 
   permissions mapping to it.
   
   Permission is role based rather than user-based for simplicity. Also permission is per resource, meaning, 
   applicable to each individual data set, be it User or Visit or MedicalStudy, etc.
   
   The only exception to this is the user creation, which requires no permission at all.
   Once a user is created with a role, she automatically inherits the permission pertaining to the role.
   Once a user is login, a token is assigned to the session and it is passed along with each request
   (operation on dataset). And handler verifies the user's permission before performing the operation.
   Token shall have an expiry date, which is currently not enforced in the current implementation.
   
   
 







