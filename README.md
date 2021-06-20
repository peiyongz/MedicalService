# MedicalService High Level Design

1. Architecture

   1.1 Load Balancer
       
       Load balancer is used to archive both load balancing and avoid single point failure.
       
       option1 stateless FE
           A service request will be routed to one of the FE servers in that service cluster.
           
       option2 stateful FE
           A service request will be routed to the FE server in that service cluster based on user hash.
           
       For which option to go, refer to discussion in DB sharding.
       
   1.2 FE Servers/Cluster & Services
       
       Services, such as UserService, VisitService, can be deployed to one or multiple FE Servers to 
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

       For which option to go, refer to discussion in DB sharding.
       
   1.3 Caching
   
       It is not expected that patient profile (User) or medical data set (Visit) change often, it fits 
       one-write-multiple-read pattern. Therefore caching is ideal for improving data retrieval.
              
       For read request, FE server (or more specifically the microservice running on it) fetch data from 
       cache first. In case of miss, it then from DB layer and the data resides in cache.
       
       For write request:
       
       option1: write-through 
       
       Since the system is expected far less write than read and it is affordable to write to both the cache 
       and the DB layer. 
       
       optoin2: write-back(aka write-deferred).
       This is good for performance and if eventual-data-consistency is acceptable in a system.
          
       We would recommend option1, write-through, mainly for avoid any 
       potential data loss/inconsistency occured during host/diaster recovery.
       
   1.4 Data Servers

       optional SQL RDBMS
       
       Relational databases are often preferred when we are concerned about the integrity of our data, 
       or when we're not particularly focused on scalability.
       
       option2 No SQL
       
       No SQL is mainly for schema-free, scalability, high availabilitythe, and eventual consistency. 
       
       Given most information the system process are structured data set, human generated (instead of
       automatically generated), We choose SQL RDBMS as the data persistence layer.
       
       Also We expect the data set will be high. To better address capacity/performance, DB Sharding 
       will be implemented.       
       
2. Schema
    
    See package model.
    
3. Auditing
    
    Auditing is enabled to capture the change history. Any change (create, update) in User/Visit 
    automatically add an entry in AuditStore.
     
    The AuditStore is generic and can be used for other data set auditing.
 
4. Anonymized data 
    
    Anonymized data can be collected by extracting each participating patient's non-personal-identifying 
    data (from both User Store and Visit Store).
     
    
    This extraction can run on a pre-defined interval like a cron job, and extra participting patients new data
    since last extraction.
    
5. Deployment

    Use Terraform to define the deployment manifests.
    Use Spinnaker pipeline to deploy various resources, such as Load Balancer, Target Group, IAM Role and
    Policy, Hosts from ami-images, ASG to maintain the min/max nodes of the service, etc.
    
    The deployed hosts can be AWS EC2, ECS or EKS, or the Azure/GCP equivalents.

6. Permission

   Permission is role based rather than user-based for simplicity. Also permission is per resource, meaning, 
   applicable to each individual data set, be it User or Visit or MedicalStudy, etc.
   
   There will be four permissions (for each resource), CREATE, RETRIEVE, UPDATE and DELETE.
   Each role, for a given resource, will have a fixed value on the four operations listed above.
   
   The only exception to this is the user creation, which requires no permission at all.
   
   Once a user is created with a role, she automatically inherits the permission pertaining to the role.
   
   Once a user is login (authentication), a token is assigned to the session and the token is passed along 
   with each request (operation on data set). And service request handler verifies the user's permission 
   (authorization) before performing the operation.
   
   Token shall have an expiry date, which is not enforced in the current implementation.
