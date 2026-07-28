import { SideNavData } from "../model/sideNavData.model";

export const instructorNavbarData: SideNavData[] = [
  {
    label: 'home_page',
    icon: 'home',
    iconColor: 'light',
    routeLink: '/'
  },

  {
    label: 'dashboard',
    icon: 'dashboard',
    iconColor: 'light',
    routeLink: '/instructor/dashboard'
  },

  {
    label: 'notifications',
    icon: 'notifications',
    iconColor: 'light',
    routeLink: '/instructor/notifications'
  },
  {
    label: 'chang_time_requests',
    icon: 'access_time',
    iconColor: 'light',
    routeLink: '/instructor/changeTimeRequests'
  },
  {
    label: 'calendar',
    icon: 'date_range',
    iconColor: 'light',
    expanded: false,
    routeLink: '/instructor/financialReports',
    items: [
      {
        label: 'week_calendar',
        icon: 'calendar',
        iconColor: 'primary',
        routeLink: '/instructor/weekCalendar',
      },
      {
        label: 'my_today_sessions',
        icon: 'book',
        iconColor: 'light',
        routeLink: '/instructor/upcomingMeetings',
      },
      {
        label: 'waiting_sessions',
        icon: 'book',
        iconColor: 'light',
        routeLink: '/instructor/attendedSessions',
      },

      {
        label: 'free_sessions',
        icon: 'book',
        iconColor: 'light',
        routeLink: '/instructor/freeSessions',
      },
    ]
  },
  {
    label: 'subscriptions',
    icon: 'subject',
    iconColor: 'primary',
    routeLink: '/instructor/newSubscriptions',
  },
  {
    label: 'messages',
    icon: 'message',
    iconColor: 'primary',
    routeLink: '/instructor/chat',
  }
];
