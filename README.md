# MedicalService High Level Design

1. Architecture

   1.1 Load Balancer
       
       Load balancer is used to archive load balancing, avoid single point failure.
       If 
       
   1.2 FE Server/Cluster and Cache
       
       Services, such as UserService, VisitService, can be deployed to 1 or multiple FE Servers to 
       handle service-specific requests.
       
       A FE server may host 1 or multiple Services. 
       FE servers hosting the same service is logically a service cluster.
       
       If sharding is used in Data Servers, then FE server will be assigned to requests for a 
       particular shard. e.g based on userName hash.
       Optionally FE server can retrieve data from cache instead of from DB directly.
       

   1.3 Data Servers

       Given most information the system process are structured data set, human generated (instead of
       automatically generated), a RDBMS can be used for data persistence layer.
       
       In the case of high volumn of data need to be stored/processed, we can do sharding to cater
       for volumn scaling as well reducing retrieval latency.
       
       Since we don't expect patient profile and medical data set change often, it fits 
       1-write-multiple-read pattern. Therefore caching (for each sharding) can be used for FE server 
       for viewing request  
       
2. Scheme
    
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

   
      








