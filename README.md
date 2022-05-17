# Mutants API 

Diagrama del servicio el cual se encuentra en infraestructura AWS
![Diagrama](/images/Mutant_Diag.png)


Tenemos un API Gateway que hace llamado a dos Lambda Funtions.
1. La primera realiza la validacion del adn para determinar si se trata de un mutante y almacena la cadena y la respuesta en Base de datos:
   ![Dynamo](/images/Dynamo_Mutants.PNG)
2. La segunda realiza la consulta de las estadisticas de los analisis realizados.


En el repositorio se encuentra:

- `src/main` - codigo de las lambda Funtions las que tienen un Handler cada una: HandlerMutant.java y HandlerMutantAnalisysStats.java.
- `src/test` - Pruebas unitarias
- `pom.xml` -  Archivo pom para el build con Maven.


# API 

Servicio verificar ADN y guardar en BD    :

https://33m8f61yji.execute-api.us-east-1.amazonaws.com/Test/mutantsFunction/mutant

Metodo : POST
Body Ejemplo: { "dna": [ "AAAA", "AAAA", "AAAA","AAAA"] } 
Respuestas:
      - 200 OK :  Cuando se realiza una validacion de adn mutante (guarda el registro en la bd)
          ![Dynamo](/images/200.PNG)
      - 403 Forbidden : Cuando el adn ingresado no es mutante
          ![Dynamo](/images/403.PNG)
      - 400 Bad Request: Cuando el servicio es invocado con una matriz no valida (Matriz no cuadrada)
          ![Dynamo](/images/400.PNG)


Servicio consultar estadisticas d elos analisis realizados:

 https://33m8f61yji.execute-api.us-east-1.amazonaws.com/Test/mutantsFunction/stats
 
 Metodo : GET
 Respuestas: {"numberOfMutants": 3.0, "numberofAnalisys": 6.0, "ratio": 0.5 }
  ![Dynamo](/images/stats.PNG)
  
  
Se adjunta Una PostMan Collection con los consumos de los servicios  (MutantsCollection.postman_collection.json)


# Deploy 

El proyecto se compila a traves del comando  `maven install`  y genera un jar (se encuentra en la carpeta target) el cual se despliega en cada Lambda en AWS, 
tambien se debe indicar la ruta del Handler que : 
![LambdaDeploy](/images/Lambda_Deploy.PNG)




