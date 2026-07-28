export const LOOKUPS = [
    {
        key:1,
        value:"contract_1_1",
        type:"CONTRACT_TYPES"
    },
    {
        key:2,
        value:"contract_2",
        type:"CONTRACT_TYPES"
    },
    {
        key:3,
        value:"contract_3",
        type:"CONTRACT_TYPES"
    },

    /*  */

    {
        key:1,
        value:"لائق",
        type: "STATUS_TYPES"
    },
    {
        key:2,
        value:"عدم لياقة مؤقتة",
        type: "STATUS_TYPES"
    },
    {
        key:3,
        value:"اجازة وضع",
        type: "STATUS_TYPES"
    },
    {
        key:4,
        value:"اصابة عمل",
        type: "STATUS_TYPES"
    },
    {
        key:5,
        value:"ايقاف",
        type: "STATUS_TYPES"
    },
    {
        key:6,
        value:"ايقاف امني",
        type: "STATUS_TYPES"
    },
    {
        key:7,
        value:"الحاق",
        type: "STATUS_TYPES"
    },
    {
        key:8,
        value:"بدون مرتب",
        type: "STATUS_TYPES"
    },
    {
        key:9,
        value:"عدم لياقة مستديمة",
        type: "STATUS_TYPES"
    },
    {
        key:10,
        value:"اعارة",
        type: "STATUS_TYPES"
    },
    {
        key:11,
        value:"الاجنبي",
        type: "STATUS_TYPES"
    },
    {
        key:12,
        value:"اداري",
        type: "STATUS_TYPES"
    },
    {
        key:13,
        value:"انهاء خدمة",
        type: "STATUS_TYPES"
    },
    {
        key:14,
        value:"محكمة عالمية",
        type: "STATUS_TYPES"
    },
    /*  */

    {
        key: 1,
        value:"رقم قومي",
        type: "IDENTIFICATION_TYPES"
    },
    {
        key: 2,
        value:"جواز سفر",
        type: "IDENTIFICATION_TYPES"
    },
    {
        key: 3,
        value:"رخصة",
        type: "IDENTIFICATION_TYPES"
    },

    /*  */
    {
        key:1,
        value:"adminstrataive_orders",
        singleValue: "أمر اداري",
        type: "DOCUMENT_FOLDERS",
        
    },
    {
        key:2,
        value:"excutive_orders",
        singleValue: "أمر تنفيذي",
        type: "DOCUMENT_FOLDERS"
    },
    {
        key:3,
        value:"office_orders",
        singleValue: "أمر مكتبي",
        type: "DOCUMENT_FOLDERS"
    },
    {
        key:4,
        value:"decisions",
        singleValue: "قرار",
        type: "DOCUMENT_FOLDERS"
    },
    {
        key:5,
        value:"other_orders",
        singleValue: "اخري",
        type: "DOCUMENT_FOLDERS"
    },

    /*  */
    {
        key:1,
        value: "الحاقات",
        type: "DOCUMENT_TYPES"
    },
    {
        key:2,
        value: "اشراف",
        type: "DOCUMENT_TYPES"
    },
    {
        key:3,
        value: "تدريب",
        type: "DOCUMENT_TYPES"
    },
    {
        key:4,
        value: "نقل",
        type: "DOCUMENT_TYPES"
    },
    {
        key:5,
        value: "انهاء خدمة",
        type: "DOCUMENT_TYPES"
    },
    {
        key:6,
        value: "جزائات",
        type: "DOCUMENT_TYPES"
    },
    {
        key:7,
        value: "اجازة بدون مرتب",
        type: "DOCUMENT_TYPES"
    },
    {
        key:8,
        value: "محو اّثار",
        type: "DOCUMENT_TYPES"
    },
    {
        key:9,
        value: "لفت نظر",
        type: "DOCUMENT_TYPES"
    },
    {
        key:10,
        value: "خصم حافز",
        type: "DOCUMENT_TYPES"
    },
    {
        key:11,
        value: "خصم محدد",
        type: "DOCUMENT_TYPES"
    },
    {
        key:12,
        value: "خصم ايام",
        type: "DOCUMENT_TYPES"
    },
    {
        key:13,
        value: "توصيات طبية",
        type: "DOCUMENT_TYPES"
    },

    {
        key:1,
        value: "home",
        type: "COMMUNICATION_TYPES"
    },
    {
        key:2,
        value: "business",
        type: "COMMUNICATION_TYPES"
    },
]

export const CONTRACT_TYPES =  LOOKUPS.filter(row=> row.type === 'CONTRACT_TYPES');

export const STATUS_TYPES = LOOKUPS.filter(row=> row.type === 'STATUS_TYPES');

export const IDENTIFICATION_TYPES = LOOKUPS.filter(row=> row.type === 'IDENTIFICATION_TYPES');

export const DOCUMENT_FOLDERS = LOOKUPS.filter(row=> row.type === 'DOCUMENT_FOLDERS');

export const DOCUMENT_TYPES =  LOOKUPS.filter(row=> row.type === 'DOCUMENT_TYPES');
export const COMMUNICATION_TYPES =  LOOKUPS.filter(row=> row.type === 'COMMUNICATION_TYPES');