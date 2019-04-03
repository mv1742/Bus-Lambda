# Bus-Lambda
A simple BusTime app that uses Raspi + AWS Lambda

### Real Time Transit Data (via https://www.streetsdatacollaborative.org/technical-overview/)

![ A $10 Raspberry Pi Zero is a fully-functional wifi-equipped computer capable of retrieving its location. ](https://static1.squarespace.com/static/59948729a803bbad877d588e/t/5997b872f14aa1178c60949c/1503115399348/raspberry-pi-zero-w-wireless-256x256.png?format=1500w)

A $10 Raspberry Pi Zero is a fully-functional wifi-equipped computer capable of retrieving its location.

![ A recent ride on a New York City bus using the $10 Raspberry Pi device and Wifi-positioning to retrieve the bus's location. A citywide system can be built to develop real time bus schedule infrastructure at the fraction of what it costs today. ](https://static1.squarespace.com/static/59948729a803bbad877d588e/t/5997b8bdf14aa1178c609703/1503115491956/bustime.jpg?format=1500w)

A recent ride on a New York City bus using the $10 Raspberry Pi device and Wifi-positioning to retrieve the bus's location. A citywide system can be built to develop real time bus schedule infrastructure at the fraction of what it costs today.

Access to real-time transit information has been linked to  [overall satisfaction with transit service](http://trrjournalonline.trb.org/doi/abs/10.3141/2082-13), [increases in ridership](http://www.sciencedirect.com/science/article/pii/S0968090X12000022), and  [substantial increases in farebox revenue](http://www.sciencedirect.com/science/article/pii/S0968090X15000297). If cities could simply increase practical availability to transit information, they could achieve outcomes similar to increases in transit service itself. Encouragingly, this missing layer of coordination between providers and users amounts to a conceptually simple piece of technology.

Less encouragingly, the legacy technology in this space can be exorbitantly expensive. NYC’s bus-tracking GPS system has been quoted at $20k per bus. After a protracted 3 year deployment process, Melbourne, a moderately-sized city, was forced to  [suspend rollout](https://www.streetsdatacollaborative.org/technical-overview/[https://www.itnews.com.au/news/melbourne-takes-second-stab-at-gps-bus-tracking-381093) of it’s original bus-tracking system in 2013 at only 30% coverage due to unreasonable operating costs. While all cities and citizens could benefit from a real-time transit information system in principle, this is not an option for all cities in practice. As forward-looking municipalities, we have to find a new approach.

To address the situation facing our cities, the StDC is leveraging the recent explosion in low-cost sensor technology to develop a BusTime4All. Simple microcomputers with internet access enable wifi-positioning at the accuracy of a few meters at costs orders of magnitude cheaper than legacy GPS systems. With SB1 committing $7.5 billion for transit operations and capital, and the StDC’s legacy-breaking approach, CA cities have a unique opportunity to increase accessibility to their transit systems as well as increase farebox revenue from this increased ridership.

**Further Reading**

-   [Real-Time Transit Data Is Good for People and Cities. What’s Holding This Technology Back?  **World Resources Institute, February 2016**](http://www.wri.org/blog/2016/02/real-time-transit-data-good-people-and-cities-whats-holding-technology-back).
-   [The Impact Of Real-Time Information On Bus Ridership In New York City. Brakewood et. al. **ScienceDirect. 2017.**](http://www.sciencedirect.com/science/article/pii/S0968090X15000297.)
-   [Ridership Effects Of Real-Time Bus Information System: A Case Study In The City Of Chicago.  **Tang et. al. Sciencedirect 2017.**](http://www.sciencedirect.com/science/article/pii/S0968090X12000022.)
-   [Examination Of Traveler Responses To Real-Time Information About Bus Arrivals Using Panel Data |  **Transportation Research Record: Journal Of The Transportation Research Board". 2017.**](http://trrjournalonline.trb.org/doi/abs/10.3141/2082-13.)
