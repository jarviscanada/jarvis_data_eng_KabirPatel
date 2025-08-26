#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
	echo "Illegal number of parameters"
	exit 1 
fi

vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

memory_free=$(echo "$vmstat_mb" | awk '{print $4}' | tail -n1 | xargs)
cpu_idle=$(vmstat --unit M | tail -1 | awk '{print $15}')
cpu_kernel=$(vmstat --unit M | tail -1 | awk '{print $14}')
disk_io=$(vmstat -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -BM ~/ | tail -1 | awk '{gsub(/M/,"",$4); print $4}')

timestamp=$(date '+%Y-%m-%d %H:%M:%S')

host_id="(SELECT id FROM host_info WHERE hostname='$hostname')";

insert_stmt="INSERT INTO host_usage(timestamp, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available, host_id) VALUES('$timestamp', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available', $host_id)"

export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?
