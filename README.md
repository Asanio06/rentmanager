# rentmanager
This is a School exercice

>## Comment lancer l'application web?

Avant tout, exécutez la commande `mvn clean install` 

- Si vous souhaitez lancez l'application web, utilisez `mvn tomcat7:run`
- Si vous souhaitez lancez l'application en interface de commande:
>-  utilisez `mvn java:exec` si c'est le premier lancement la premiere fois
>- Dans le pom.xml ,cherchez le plugin "exec-maven-plugin" puis dans le "mainClass" changez "com.epf.rentmanager.persistence.FillDatabase" par "com.epf.rentmanager.ui.UI" ; 
>- Ensuite lancer la commande `mvn java:exec`                                                                                                                                       



>## Difficultés 

La fonction la plus difficile à faire a été celle permettant de vérifier qu'un véhicule n'était pas réserver plus de 30 jours de suite sans repos.
