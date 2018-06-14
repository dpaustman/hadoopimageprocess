# -*- mode: ruby -*-
# vi: set ft=ruby :

$script = <<SCRIPT
echo Started to provision...
date > /etc/vagrant_provisioned_at
SCRIPT


Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: $script

  config.vm.define "slave1" do |slave1|
    slave1.vm.box = "ubuntu/trusty64"
    slave1.vm.box_url = "https://atlas.hashicorp.com/ubuntu/boxes/trusty64"
    slave1.vm.host_name = "slave1"

    slave1.vm.synced_folder "scripts", "/mnt/bootstrap", :create => true
    slave1.vm.provision :shell, :path => "scripts/hadoop-slave.sh"

    slave1.vm.network "private_network", ip: "192.168.205.12"
  end

  config.vm.define "master" do |master|
    master.vm.box = "ubuntu/trusty64"
    master.vm.box_url = "https://atlas.hashicorp.com/ubuntu/boxes/trusty64"
    master.vm.host_name = "master"

    master.vm.synced_folder "scripts", "/mnt/bootstrap", :create => true
    master.vm.provision :shell, :path => "scripts/hadoop-master.sh"

    master.vm.network "forwarded_port", guest: 8088, host: 8088
    master.vm.network "forwarded_port", guest: 8030, host: 8030

    master.vm.network  "private_network", ip: "192.168.205.11"
  end

  config.vm.provider "virtualbox" do |vb|
    vb.customize ["modifyvm", :id, "--memory", "2048"]
  end



end