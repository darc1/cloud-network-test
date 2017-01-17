#!/bin/bash
# demo
#
# description: demo service

case $1 in
    start)
        /bin/bash /usr/local/bin/start_demo_service.sh
    ;;
    stop)
        /bin/bash /usr/local/bin/stop_demo_service.sh
    ;;
    restart)
        /bin/bash /usr/local/bin/stop_demo_service.sh
        /bin/bash /usr/local/bin/start_demo_service.sh
    ;;
esac
exit 0