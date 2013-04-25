Algoritmos Distribuídos
=======================

Exercícios da disciplina de algoritmos distribuídos ministrada por Gustavo M. D. Vieira oferecida pelo programa de mestrado da UFSCar - Campus Sorocaba

## Criando o projeto

1. Instale o [maven](http://maven.apache.org/download.cgi)

2. Configure as varáveis de ambiente $JAVA_HOME e $M2_HOME

3. Instale o appia no seu repositório local utilizando o seguinte comando:
	$ mvn install:install-file -DgroupId=net.sf.appia -DartifactId=appia-core -Dpackaging=jar -Dversion=4.1.2 -Dfile=appia-core-4.1.2.jar -DgeneratePom=true

4. Monte o projeto para o eclipse com o seguinte comando:
  $ mvn eclipse:eclipse

5. Instale o lombok no seu eclipse com o seguinte comando:
	$ java -jar ~/.m2/repository/org/projectlombok/lombok/0.9.3/lombok-0.9.3.jar

6. Importe o projeto para o eclipse 

## Lista de exercícios usando appia

Exercício 1: br.ufscar.sorocaba.appia.services.job

Exercício 2: br.ufscar.sorocaba.appia.datalink.fairloss

Exercício 3: br.ufscar.sorocaba.appia.datalink.perfect