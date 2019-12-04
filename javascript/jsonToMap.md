# JavaScript Json Object를 Map으로 파싱

서버에서 전달 받는 key - value 데이터를 편하게 다루기 위하여 변환, Get함수 사용 가능.
```
const map = new Map(Object.entries({foo: 'bar'}));

map.get('foo'); // 'bar'
```