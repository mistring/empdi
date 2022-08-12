# empdi

Employee Directory

## (Build tools & versions used)

I created this app using Android Studio and compiled against Android SDK version 32. The minimum
supported SDK is version 24, to maximize the number of compatible devices. Various google
dependencies (and Kotlin coroutines) were used, as well as:

- Glide (4.11.0) for image fetching, caching, and manipulation
- Timber (5.0.1) for logging, with a custom DebugTree that I've used for several years
- Retrofit (2.9.0) with a gson converter and logging interceptor

## (Steps to run the app)

The sourcecode for this project is found
in [this public GitHub repository](https://github.com/mistring/empdi). When the repository is pulled
down, it can be opened in Android Studio, and run via emulator or physical device.

## (What areas of the app did you focus on?)

I focused on the following areas:

- MVVM Architecture (Dagger Hilt, Retrofit, Coroutines/Flow)
- UI (RecyclerView, ListAdapter, layouts, custom fonts, launcher icons)

## (What was the reason for your focus? What problems were you trying to solve?)

- In terms of MVVM, I wanted to have a good framework for providing dependencies.
- Using Hilt also makes adding future screens easier, as classes can be provided with minimal
  effort. Also, Hilt is more lightweight than Dagger2 which simplifies the code.
- Retrofit is a great library for making API calls, as well as marshalling/unmarshalling JSON data.
  It also allows for logging interception for greater insights into the requests and responses for
  each call. I could have used something like Charles or mitmproxy, but those are more involved.
- The coroutines and FlowCollectors are a great help for asynchronous work and when you need to do
  work on a different thread (or Dispatcher), depending on whether that work is merely CPU intensive
  or involves IO operations. I wanted to avoid RxJava-type libraries, as they are not the
  recommended route for reactive and asynchronous programming. Also, LiveData is being superseded by
  Coroutines/Flow in many instances, so I wanted to use best practices for network requests and
  reactive programming portions of the code. This includes the relationship between a View and a
  ViewModel.
- The UI is a major part of why I enjoy building apps. I care about the architecture and supporting
  details behind the scenes, but the majority of a successful user experience is based on the UI
  components and design. I stayed as native as possible, and avoiding libraries such as Epoxy. 3rd
  party libraries can be a great help but come with their own risks.

## (How long did you spend on this project?)

I spent close to 6 hours on this project, most of which were on a Saturday. Some of the time was
deciding which functional routes to take, and ensuring that I wasn't adding more functionality than
described in the requirements document.

## (Did you make any trade-offs for this project? What would you have done differently with more time?)

I focused on teh architecture and the UI the app, and kept the unit tests for the last. If more time
were available, and other teams such as Product, Design, and Testing were available, I would have
coordinated with these groups before plunging into the UX of the solution. The result of this was a
quicker solution, but the trade-offs were that everything was done according to my understanding of
the desired outcome.

## (What do you think is the weakest part of your project?)

The testing framework has a solid foundation (including some helper classes), but needs more work to
cover the ViewModel, etc.

## (Did you copy any code or dependencies? Please make sure to attribute them here!)

- I re-used some code that I learned from
  a [Udemy course](https://www.udemy.com/course/android-11-tdd-masterclass/) that I took a few
  months ago. This was used to help provide a foundation to the unit tests.
- Also, I learned about the Flow extension functions from a few courses on Udemy and from
  researching the Google Android documentation and stackoverflow.

## (Is there any other information you’d like us to know?)

While working on the UI and Networking components, I made use of a Mock Rest API Framework
called [Mockoon](https://mockoon.com/) to help with testing against various data formats. It helped
to simulate empty, malformed, and successful JSON responses without having to wire up multiple paths
to trigger the various endpoints provided. Eventually, I did add menu items to exercise the live
endpoints, but I was able to make great progress without having the server wired up for each
endpoint. This allowed me to focus on a targeted aspect of the code at a time. Instead of having a
complete netowrk, repository, and view layer, I can build them incrementally. 
