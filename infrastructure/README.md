# PolyFrontier Infrastructure

## Server

The server is the base of our infrastructure. It runs as a Debian 11 Proxmox VM (LXC container) on a dedicated machine in Paris 

## Packages
``nginx`` => web server => simple and lightweight

``cloudflared`` => tunnel to the exterior (see below) => secure, edge of tech

``docker`` => to run PostgreSQL and pgAdmin => ease of upgrade and data retention

...

## Services
``Cloudflare`` => protection and CDN

``Cloudflare for Teams`` => access control and protection

``GitHub Developers`` => OAuth app to authenticate admins


## Communications
The server is mainly closed to the outside meaning all communications entering the server (with the exception of PostgreSQL) are DROPPED by the firewall.

### Web 
To open the server to the web, we are using Cloudflare's Tunnels, a great way to serve content as an ingress only solution. This way, all the DDoS protection, basic WAF and SSL are configured through this service.

### SSH
To use SSH we use a SSH rendered with Cloudflare Tunnels once again, available at the address ssh.polyfrontier.me.

### pgAdmin
To administrate PostgreSQL, we use pgAdmin. The service is available at db.polyfrontier.me.

## Authentication
To use the SSH and pgAdmin services, we are using Cloudflare Access to monitor and authorize access granularly. This way, only people authorizd by the administrator can access those services among others with a strict application of the policies applied.
