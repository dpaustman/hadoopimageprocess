# Hadoop image processor

The application is a template for map-reduce image processing solution. 
It can be used for processing a big number of images, stored into Hadoop storage.

The document contains the instructions about how to deploy 2-nodes Hadoop cluster locally. 
It also includes the steps of how to build and submit Image Processing mapreduce job written in Java. 
Please, read carefully the document and perform all listed steps in order to deploy the cluster without errors.

# Prerequisites for Windows:

1. Install SSH client.

    Choose on of the options to install ssh client CLI

        * Cygwin + opennssh package to execute commands from console
        
        * MobaXterm SSH client: http://mobaxterm.mobatek.net/download-home-edition.html
        
        * PuTTY: https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html

2. Install Virtual Box: http://www.oracle.com/technetwork/server-storage/virtualbox/downloads/index.html

3. Reboot your PC

4. Install Vargant: https://www.vagrantup.com/downloads.html

# Build application

1. Install jdk 1.8
2. Clone the project

    $ git clone https://username@bitbucket.org/Serg_Rubtsov/hadoopimageprocess.git

2. Build jar file

    $ cd hadoopimageprocess/

    $ gradlew.bat jar

3. Copy jar file from `build/libs/hadoopimageprocess-1.0-SNAPSHOT.jar` to `scripts/`

	$ cp build/libs/hadoopimageprocess-1.0-SNAPSHOT.jar scripts/

# Running Hadoop cluster

## Run 2 servers

    $ vagrant up slave1 master 

    # if everything is ok you can visit http://192.168.205.11:8088/cluster/nodes and see 2 active nodes

## SSH to master(slave) and switch to hadoop user

    $ vagrant ssh master
    $ sudo su hadoopuser

## Create input/output directories and upload files to HDFS
    
    $ hadoop fs -mkdir /input
    $ hadoop fs -chmod -R 777 /
    $ hadoop fs -copyFromLocal /mnt/bootstrap/input/* /input
    
    # Please note that image is located in `scripts/input/img.bmp` folder of your project

# Run mapreduce

1. Run mapreduce sample from a vm with hadoop being installed on it

    $ hadoop jar /mnt/bootstrap/hadoopimageprocess-1.0-SNAPSHOT.jar /input /output

2. Download result from Hadoop file system: http://192.168.205.11:50070/explorer.html#/output

# Known issues:

When downloading result hadoop uses `master` hostname, please replace this name with IP `192.168.205.11`.

# Debug start:

    $ hadoop jar /mnt/bootstrap/hadoopimageprocess-1.0-SNAPSHOT.jar -D mapreduce.map.java.opts="-Xmx480m -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" /input /output