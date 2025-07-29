#!/bin/bash

# Fix the paths to reference the database directory
psql -U postgres -f database/cleanup.sql 
psql -U postgres -f database/init.sql 