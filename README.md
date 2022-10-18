# README

My minecraft server, this time dockerized and simplified for the cloud.

## Future Features

- [ ] Configure which port the interaction server runs on
  - This probably should be an environment variable set in the docker file
  - Alternatively, this could just be an external configuration on whatever network docker runs on
- [ ] String with list of online players?
- [ ] Discord server connection?
- [ ] A `help` request that responds with all the valid commands available
- [ ] Expand this to also contain all the other pieces we need to setup on AWS
  - [ ] Terraform changes to be applied on AWS
  - [ ] Web app that allows remote startup/status information
  - [ ] Service that generates maps from one docker volume and ensures they are visible on the site
