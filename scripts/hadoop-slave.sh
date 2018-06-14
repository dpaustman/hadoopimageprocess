#!/usr/bin/env bash

# disable ipv6 in ubuntu
echo "net.ipv6.conf.all.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.default.disable_ipv6 = 1" >> /etc/sysctl.conf
echo "net.ipv6.conf.lo.disable_ipv6 = 1" >> /etc/sysctl.conf

#This package provides the add-apt-repository binary, which you will need to
#install a new ppa easily
sudo apt-get -y install python-software-properties

# openjdk
sudo apt-get -y update
sudo apt-get -y install openjdk-7-jdk
sudo ln -s /usr/lib/jvm/java-1.7.0-openjdk-amd64 /usr/lib/jvm/java

sudo apt-get -y install dos2unix

# Create hadoopgroup
sudo addgroup hadoopgroup
# Create hadoopuser user
sudo adduser hadoopuser --gecos "First Last,RoomNumber,WorkPhone,HomePhone" --ingroup hadoopgroup --disabled-password
echo "hadoopuser:password" | sudo chpasswd

# change to hadoopuser
sudo -u hadoopuser bash << EOF

mkdir -p /home/hadoopuser/.ssh

chmod 700 /home/hadoopuser/.ssh/

touch /home/hadoopuser/.ssh/authorized_keys

chmod 640 /home/hadoopuser/.ssh/authorized_keys

cat /mnt/bootstrap/hadoop_files/id_rsa.pub >> /home/hadoopuser/.ssh/authorized_keys

#unpack hadoop
cd /home/hadoopuser
# wget http://www.eu.apache.org/dist/hadoop/core/hadoop-2.8.0/hadoop-2.8.0.tar.gz
wget http://www-us.apache.org/dist/hadoop/common/hadoop-2.8.0/hadoop-2.8.0.tar.gz

#cp /mnt/bootstrap/hadoop-2.8.0.tar.gz /home/hadoopuser/
tar xvf hadoop-2.8.0.tar.gz
mv hadoop-2.8.0 hadoop

export HADOOP_HOME=/home/hadoopuser/hadoop
export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin

#set enviroment variables
cp /mnt/bootstrap/hadoop_files/.bashrc /home/hadoopuser/.bashrc
dos2unix /mnt/bootstrap/hadoop_files/.bashrc /home/hadoopuser/.bashrc

cp /mnt/bootstrap/hadoop_files/hadoop-env.sh /home/hadoopuser/hadoop/etc/hadoop/hadoop-env.sh
dos2unix /mnt/bootstrap/hadoop_files/hadoop-env.sh /home/hadoopuser/hadoop/etc/hadoop/hadoop-env.sh
cp /mnt/bootstrap/hadoop_files/core-site.xml /home/hadoopuser/hadoop/etc/hadoop/core-site.xml
dos2unix /mnt/bootstrap/hadoop_files/core-site.xml /home/hadoopuser/hadoop/etc/hadoop/core-site.xml
#create directories for master and slave
mkdir -p /home/hadoopuser/hadoop-data/hadoopuser/hdfs/namenode
mkdir -p /home/hadoopuser/hadoop-data/hadoopuser/hdfs/datanode

cp /mnt/bootstrap/hadoop_files/hdfs-site.xml /home/hadoopuser/hadoop/etc/hadoop/hdfs-site.xml
dos2unix /mnt/bootstrap/hadoop_files/hdfs-site.xml /home/hadoopuser/hadoop/etc/hadoop/hdfs-site.xml
cp /mnt/bootstrap/hadoop_files/yarn-site.xml /home/hadoopuser/hadoop/etc/hadoop/yarn-site.xml
dos2unix /mnt/bootstrap/hadoop_files/yarn-site.xml /home/hadoopuser/hadoop/etc/hadoop/yarn-site.xml

EOF