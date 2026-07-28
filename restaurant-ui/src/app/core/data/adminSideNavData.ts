import { SideNavData } from "../model/sideNavData.model";

export const adminNavbarData: SideNavData[] = [

  {
    label: 'dashboard',
    icon: 'home',
    iconColor: 'light',
    routeLink: '/admin/dashboard'
  },
  {
    label: 'users',
    icon: 'group',
    iconColor: 'light',
    routeLink: '/admin/users'
  },
  {
    label: 'products',
    icon: 'shopping_cart',
    iconColor: 'light',
    routeLink: '/admin/products'
  },


  {
    label: 'settings',
    icon: 'settings',
    iconColor: 'light',
    items:[
      {
        label: 'settings',
        icon: 'settings',
        iconColor: 'light',
        routeLink: '/admin/settings',
      },
      {
        label: 'whatsappApi',
        icon: 'phone',
        iconColor: 'light',
        routeLink: '/admin/whatsappApi',
      },
      {
        label: 'calendars',
        icon: 'settings',
        iconColor: 'light',
        routeLink: '/admin/calendars',
      },
      {
        label: 'salaries',
        icon: 'attach_money',
        iconColor: 'light',
        routeLink: '/admin/salaries',
      },
      {
        label: 'المرتبات الجديدة',
        icon: 'attach_money',
        iconColor: 'light',
        routeLink: '/admin/salariesNew',
      },
      {
        label: 'prices',
        icon: 'attach_money',
        iconColor: 'light',
        routeLink: '/admin/prices',
      },
       {
        label: 'الخصومات',
        icon: 'attach_money',
        iconColor: 'light',
        routeLink: '/admin/discounts',
      }
    ]
  },

];
