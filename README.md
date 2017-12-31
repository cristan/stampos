# StamPOS
This is a bar system designed for venues where you know your customers. It has the following goals:

- So user friendly, that the most atechnical person in your group can still order a drink
- Drastically reduce the amount of money your customers owe you
- Automate the administrative tasks of running a bar as much as possible

## User friendly
Click on your name, click on the products you want to order, press "Confirm" and you're all set. It can't get much more user friendly than this.

## Reduces the amount your customers owe you
In most systems, people start with 0 debt to the bar, and with every drink, this increases. Then after a while, people pay their debt and they have 0 again. This means that in any point in time, the bar is owed quite a bit of money.

In StamPOS, this works differently: people are expected to have credit with the bar. When they run out, they will be blocked, they can only order during the same weekend, but that's it. They could just pay their bill, but that means they can only order for 1 weekend again. It's handier for your customers to wire more money than they actually owe, so they can order for more than just 1 weekend.

In the off chance a customer hasn't paid, but bar personell still wants to allow ordering for him/her, there's an unblock functionality.

## Automates administrative tasks
The following is automated:
- Sending an e-mail to all applicable users with their balance with the bar
- Determine which users have paid and which amount (using an export of internet banking)
- Sending a weekly e-mail to the owner of the bar with a backup of the database and a list of all users and their balance

# Installing
- Download and install Tomcat 8. For most convenience, set the port number to 80
- Download [the latest version of StamPOS](https://github.com/cristan/stampos/releases/latest), rename it to ROOT.war and deploy it to Tomcat. You can do this by placing it in /webapps in the Tomcat installation directory, or by using the built-in manager.
- Go to [localhost](http://localhost) to see your installation :)