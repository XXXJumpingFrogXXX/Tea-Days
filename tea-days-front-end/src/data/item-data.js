let $item = {
    items: [
        {name: '龙井叶子', id: 1, category: 1, img: require('../assets/item-id-1.png'), price: 8},
        {name: '观音叶子', id: 2, category: 1, img: require('../assets/item-id-2.png'), price: 4},
        {name: '普洱叶子', id: 3, category: 1, img: require('../assets/item-id-6.png'), price: 10},
        {name: '碧螺春叶子', id: 4, category: 1, img: require('../assets/item-id-7.png'), price: 12},
        {name: '天山白叶子', id: 5, category: 1, img: require('../assets/item-id-10.png'), price: 15},
        {name: '鹿苑叶子', id: 6, category: 1, img: require('../assets/item-id-11.png'), price: 16},
        {name: '凤凰叶子', id: 7, category: 1, img: require('../assets/item-id-12.png'), price: 20},
        {name: '龙井幼苗', id: 8, category: 0, img: require('../assets/item-id-3.png'), price: 100},
        {name: '观音幼苗', id: 9, category: 0, img: require('../assets/item-id-4.png'), price: 50},
        {name: '普洱幼苗', id: 10, category: 0, img: require('../assets/item-id-8.png'), price: 150},
        {name: '碧螺春幼苗', id: 11, category: 0, img: require('../assets/item-id-9.png'), price: 200},
        {name: '天山白幼苗', id: 12, category: 0, img: require('../assets/item-id-13.png'), price: 150},
        {name: '鹿苑幼苗', id: 13, category: 0, img: require('../assets/item-id-14.png'), price: 130},
        {name: '凤凰幼苗', id: 14, category: 0, img: require('../assets/item-id-15.png'), price: 210},
        {name: '水滴', id: 15, category: 2, img: require('../assets/item-id-5.png'), price: 25},


    ],
    getItemInfo: function (id) {
        for (const item of this.items) {
            if (item.id === id) {
                return item;
            }
        }
        return -1;
    }
}

export default $item